import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import type { UserResponse } from "@/types/dtos/user";
import { UserRolesEnum, userRolesMeta } from "@/types/enums/user-roles-enum";
import { renderComponent, renderSnippet } from "@/components/ui/data-table";
import { badgeVariants } from "@/components/ui/badge";
import { createRawSnippet } from "svelte";
import DatatableLink from "@/components/datatable/datatable-link.svelte";
import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";

export const columns: (ColumnDef<UserResponse> & { sortable?: boolean })[] = [
    {
        accessorKey: "firstname",
        header: m.firstname(),
        sortable: true,
    },
    {
        accessorKey: "lastname",
        header: m.lastname(),
        sortable: true,
    },
    {
        accessorKey: "email",
        header: m.email_address(),
        sortable: true,
    },
    {
        accessorKey: "role",
        header: m.role(),
        sortable: true,
        cell: ({ row }) => {
            const meta = userRolesMeta[row.original.role];

            return renderComponent(DatatableBadgeColored, {color: meta.color, icon: meta.icon, text: meta.translation})
        },
    },
    {
        accessorKey: "id",
        header: "",
        cell: ({ row }) => {
            return renderComponent(DatatableLink, {id: row.original.id})
        }
    }
];
