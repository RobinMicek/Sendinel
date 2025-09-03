import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import { renderComponent} from "@/components/ui/data-table";
import DatatableLink from "@/components/datatable/datatable-link.svelte";
import type { ClientResponse } from "@/types/dtos/client";
import DatatableTruncatedText from "@/components/datatable/datatable-truncated-text.svelte";

export const columns: (ColumnDef<ClientResponse> & { sortable?: boolean })[] = [
    {
        accessorKey: "name",
        header: m.name(),
        sortable: true,
    },
    {
        id: "sender",
        accessorFn: (row) => row.sender,
        header: m.sender(),
        sortable: true,
        cell: ({ row }) => {
            return row.original.sender?.name
        }
    },
    {
        accessorKey: "description",
        header: m.description(),    
        cell: ({ row }) => {
            return renderComponent(DatatableTruncatedText, {text: row.original.description})
        }
    },
    {
        accessorKey: "id",
        header: "",
        cell: ({ row }) => {
            return renderComponent(DatatableLink, {id: row.original.id})
        }
    }
];
