import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import { renderComponent} from "@/components/ui/data-table";
import DatatableLink from "@/components/datatable/datatable-link.svelte";
import type { SenderResponse } from "@/types/dtos/sender";
import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";
import { Icon } from "@lucide/svelte";
import { senderTypesMeta } from "@/types/enums/sender-types-enum";

export const columns: (ColumnDef<SenderResponse> & { sortable?: boolean })[] = [
    {
        accessorKey: "name",
        header: m.name(),
        sortable: true,
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
