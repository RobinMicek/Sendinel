import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import { renderComponent} from "@/components/ui/data-table";
import DatatableLink from "@/components/datatable/datatable-link.svelte";
import type { SenderResponse } from "@/types/dtos/sender";
import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";
import { senderTypesMeta } from "@/types/enums/sender-types-enum";
import DatatableTruncatedText from "@/components/datatable/datatable-truncated-text.svelte";

export const columns: (ColumnDef<SenderResponse> & { sortable?: boolean })[] = [
    {
        accessorKey: "name",
        header: m.name(),
        sortable: true,
    },
    {
        accessorKey: "description",
        header: m.description(),
        cell: ({ row }) => {
            return renderComponent(DatatableTruncatedText, {text: row.original.description})
        }
    },
    {
        accessorKey: "type",        
        header: m.type(),
        sortable: true,
        cell: ({ row }) => {        
            const meta = senderTypesMeta[row.original.type];

            return renderComponent(DatatableBadgeColored, {color: "stroke-white", uppercase: false, text: meta.displayName})
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
