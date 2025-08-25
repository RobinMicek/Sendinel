import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import type { UserResponse } from "@/types/dtos/user";
import { UserRolesEnum } from "@/types/enums/user-roles-enum";
import { renderComponent, renderSnippet } from "@/components/ui/data-table";
import { badgeVariants } from "@/components/ui/badge";
import { createRawSnippet } from "svelte";
import DatatableLink from "@/components/datatable/datatable-link.svelte";

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
            const role = row.original.role;
            const variant = role === UserRolesEnum.ADMIN ? "default" : "secondary";

            return renderSnippet(
                createRawSnippet(() => ({
                    render: () => `<span class="font-semibold ${badgeVariants({ variant })}">${role}</span>`
                }))
            );
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
