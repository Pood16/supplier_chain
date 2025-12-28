# Keycloak User Integration - Implementation Summary

## Changes Made

### 1. Database Schema (NEW)
**File:** `src/main/resources/db/changelog/changes/008-add-keycloak-user-id.sql`
- Added `keycloak_user_id VARCHAR(255) UNIQUE` column to `users` table
- Made `password` column nullable (Keycloak users don't have local passwords)
- Added index on `keycloak_user_id` for performance

**File:** `src/main/resources/db/changelog/master.xml`
- Added reference to new changeset

### 2. Entity Update
**File:** `src/main/java/org/tricol/supplierchain/entity/UserApp.java`
- Added `keycloakUserId` field
- Made `password` field nullable

### 3. Repository Update
**File:** `src/main/java/org/tricol/supplierchain/repository/UserRepository.java`
- Added `findByKeycloakUserId(String keycloakUserId)` method

### 4. Current User Service (NEW)
**File:** `src/main/java/org/tricol/supplierchain/service/CurrentUserService.java`
- Resolves current authenticated user from SecurityContext
- For Keycloak JWT: Uses `sub` claim as keycloakUserId
- Auto-creates user on first login with:
  - `keycloakUserId` from JWT `sub` claim
  - `username` from JWT `preferred_username` claim
  - `email` from JWT `email` claim
  - `enabled` = true
  - `password` = null
  - `role` = null (roles from JWT, not DB)
- For local JWT: Falls back to username lookup

### 5. Security Aspects Updated
**File:** `src/main/java/org/tricol/supplierchain/security/AdminAuditAspect.java`
- Replaced `UserRepository.findByUsername()` with `CurrentUserService.getCurrentUser()`

**File:** `src/main/java/org/tricol/supplierchain/security/AuditAspect.java`
- Replaced `UserRepository.findByUsername()` with `CurrentUserService.getCurrentUser()`

**File:** `src/main/java/org/tricol/supplierchain/security/PermissionAspect.java`
- Replaced `UserRepository.findByUsername()` with `CurrentUserService.getCurrentUser()`
- Critical for permission checks to work with Keycloak users

**File:** `src/main/java/org/tricol/supplierchain/security/AuthAuditAspect.java`
- No changes needed (handles local authentication only)

## How It Works

### Keycloak Authentication Flow
1. User authenticates with Keycloak
2. Keycloak issues JWT with `sub` claim (user ID)
3. Request arrives with Bearer token
4. OAuth2 Resource Server validates token
5. On first request, `CurrentUserService.getCurrentUser()` is called
6. Service extracts `sub` claim from JWT
7. Looks up user by `keycloakUserId`
8. If not found, creates new user with JWT claims
9. Returns UserApp entity for audit/permission checks

### Local JWT Authentication Flow
1. User logs in with username/password
2. Application issues local JWT
3. Request arrives with Bearer token
4. JwtAuthenticationFilter validates token
5. `CurrentUserService.getCurrentUser()` falls back to username lookup
6. Returns existing UserApp entity

## Testing Checklist

### Keycloak Authentication
- [ ] First login with Keycloak token creates user automatically
- [ ] Subsequent logins reuse existing user
- [ ] User record has `keycloakUserId` populated
- [ ] User record has `password` = null
- [ ] Audit logs capture Keycloak user actions
- [ ] Permission checks work for Keycloak users
- [ ] No "User not found" errors

### Local Authentication
- [ ] Local login still works
- [ ] Local JWT validation works
- [ ] Audit logs capture local user actions
- [ ] Permission checks work for local users

### Database
- [ ] Migration runs successfully
- [ ] `keycloak_user_id` column exists
- [ ] `password` column is nullable
- [ ] Index on `keycloak_user_id` exists

## API Endpoints to Test

### With Keycloak Token
```bash
# Get users (requires ADMIN role in Keycloak)
GET /api/v1/admin/users
Authorization: Bearer <keycloak_token>
```

### With Local Token
```bash
# Login
POST /api/v1/auth/login
{
  "username": "admin",
  "password": "password"
}

# Get users
GET /api/v1/admin/users
Authorization: Bearer <local_token>
```

## Expected Outcomes

✅ Keycloak users automatically created on first login
✅ No more "User not found" errors for Keycloak users
✅ Local JWT authentication continues to work
✅ Role-based authorization works from JWT claims
✅ Audit logging works for both authentication types
✅ Permission checks work for both authentication types
✅ No breaking changes to existing functionality

## Rollback Plan

If issues occur, rollback the database migration:
```sql
ALTER TABLE users DROP COLUMN keycloak_user_id;
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NOT NULL;
```

Then revert code changes via git.
