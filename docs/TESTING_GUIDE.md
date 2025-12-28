# Manual Testing Guide - Keycloak User Integration

## Prerequisites
1. Keycloak is running (docker-compose up)
2. Application database is running
3. Liquibase migrations have been applied
4. Keycloak realm and client are configured

## Test Scenario 1: First-Time Keycloak User Login

### Steps:
1. Create a user in Keycloak (if not exists):
   - Username: `testuser`
   - Email: `testuser@example.com`
   - Assign role: `ADMIN` (in client roles)

2. Get Keycloak token:
```bash
curl -X POST "http://localhost:8080/realms/supplier-chain/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=supplier-chain-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=testuser" \
  -d "password=testuser123" \
  -d "grant_type=password"
```

3. Use token to access protected endpoint:
```bash
curl -X GET "http://localhost:8081/api/v1/admin/users?page=0&size=20" \
  -H "Authorization: Bearer YOUR_KEYCLOAK_TOKEN"
```

### Expected Results:
✅ Request succeeds (200 OK)
✅ User is auto-created in database
✅ Check database:
```sql
SELECT id, username, email, keycloak_user_id, password, enabled 
FROM users 
WHERE keycloak_user_id IS NOT NULL;
```
✅ Should see:
- `username` = "testuser"
- `email` = "testuser@example.com"
- `keycloak_user_id` = (UUID from JWT sub claim)
- `password` = NULL
- `enabled` = true

## Test Scenario 2: Subsequent Keycloak User Requests

### Steps:
1. Use the same Keycloak token from Scenario 1
2. Make another request to any protected endpoint
3. Check application logs

### Expected Results:
✅ Request succeeds
✅ No new user created
✅ Logs show: "Local JWT authentication successful for user: testuser" (or similar)
✅ Database user count unchanged

## Test Scenario 3: Local Authentication Still Works

### Steps:
1. Login with local credentials:
```bash
curl -X POST "http://localhost:8081/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

2. Use returned token:
```bash
curl -X GET "http://localhost:8081/api/v1/admin/users?page=0&size=20" \
  -H "Authorization: Bearer YOUR_LOCAL_TOKEN"
```

### Expected Results:
✅ Login succeeds
✅ Token is issued
✅ Protected endpoint access works
✅ No errors in logs

## Test Scenario 4: Permission Checks Work

### Steps:
1. Create a Keycloak user WITHOUT admin role
2. Get token for that user
3. Try to access admin endpoint:
```bash
curl -X GET "http://localhost:8081/api/v1/admin/users" \
  -H "Authorization: Bearer NON_ADMIN_TOKEN"
```

### Expected Results:
✅ Request fails with 403 Forbidden
✅ Error message: "Access denied" or similar
✅ User is still created in database (authentication succeeded)
✅ Authorization failed (as expected)

## Test Scenario 5: Audit Logs Capture Keycloak Users

### Steps:
1. Use Keycloak token to perform an audited action
2. Check audit logs:
```sql
SELECT * FROM audit_logs 
WHERE username = 'testuser' 
ORDER BY action_timestamp DESC 
LIMIT 10;
```

### Expected Results:
✅ Audit logs contain entries for Keycloak user
✅ `user_id` is populated (from auto-created user)
✅ `username` matches Keycloak username
✅ `action`, `resource`, and `details` are captured

## Troubleshooting

### Issue: "User not found" error
**Cause:** CurrentUserService not being called or JWT not parsed correctly
**Check:**
- Is the token a valid Keycloak JWT?
- Does the JWT have a `sub` claim?
- Check application logs for errors in CurrentUserService

### Issue: User created but password is NOT NULL
**Cause:** Migration didn't run or old code is still running
**Fix:**
- Check Liquibase logs
- Verify column is nullable: `DESCRIBE users;`
- Restart application

### Issue: Duplicate key error on username
**Cause:** Username from Keycloak already exists as local user
**Fix:**
- Use different username in Keycloak
- Or manually update existing user to add keycloak_user_id

### Issue: Local authentication broken
**Cause:** Code changes affected local auth flow
**Check:**
- CustomUserDetailsService still works
- JwtAuthenticationFilter still processes local tokens
- ConditionalBearerTokenResolver logic is correct

## Database Verification Queries

### Check Keycloak users:
```sql
SELECT id, username, email, keycloak_user_id, password IS NULL as is_keycloak_user, enabled
FROM users
WHERE keycloak_user_id IS NOT NULL;
```

### Check local users:
```sql
SELECT id, username, email, keycloak_user_id, password IS NOT NULL as has_password, enabled
FROM users
WHERE keycloak_user_id IS NULL;
```

### Check all users:
```sql
SELECT 
    id, 
    username, 
    email, 
    CASE 
        WHEN keycloak_user_id IS NOT NULL THEN 'Keycloak'
        ELSE 'Local'
    END as auth_type,
    enabled
FROM users
ORDER BY created_at DESC;
```
