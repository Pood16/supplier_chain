package org.tricol.supplierchain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.tricol.supplierchain.enums.RoleApp;

@Entity
@Table(name = "role_permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role", "permission_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleApp role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
