package org.tricol.supplierchain.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.tricol.supplierchain.entity.UserApp;
import org.tricol.supplierchain.repository.UserRepository;
import org.tricol.supplierchain.service.inter.AuditService;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAuditAspect {

    private final AuditService auditService;
    private final UserRepository userRepository;

    @AfterReturning(
            pointcut = "execution(* org.tricol.supplierchain.service.UserAdminServiceImpl.assignRole(..))",
            returning = "result"
    )
    public void auditRoleAssignment(JoinPoint joinPoint, UserApp result) {
        try {
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            UserApp admin = userRepository.findByUsername(adminUsername).orElse(null);

            Object[] args = joinPoint.getArgs();
            Long targetUserId = (Long) args[0];

            auditService.logAudit(
                    admin != null ? admin.getId() : null,
                    adminUsername,
                    "ASSIGN_ROLE",
                    "USER_MANAGEMENT",
                    String.format("Assigned role '%s' to user ID: %d", result.getRole(), targetUserId)
            );
        } catch (Exception e) {
            log.error("Failed to audit role assignment: {}", e.getMessage());
        }
    }

    @AfterReturning(
            pointcut = "execution(* org.tricol.supplierchain.service.UserAdminServiceImpl.modifyPermission(..))"
    )
    public void auditPermissionModification(JoinPoint joinPoint) {
        try {
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            UserApp admin = userRepository.findByUsername(adminUsername).orElse(null);

            Object[] args = joinPoint.getArgs();
            Long targetUserId = (Long) args[0];

            auditService.logAudit(
                    admin != null ? admin.getId() : null,
                    adminUsername,
                    "MODIFY_PERMISSION",
                    "USER_MANAGEMENT",
                    String.format("Modified permission for user ID: %d", targetUserId)
            );
        } catch (Exception e) {
            log.error("Failed to audit permission modification: {}", e.getMessage());
        }
    }
}
