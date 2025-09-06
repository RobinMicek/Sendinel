import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import { renderComponent } from "@/components/ui/data-table";
import DatatableLink from "@/components/datatable/datatable-link.svelte";
import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";
import DatatableTruncatedText from "@/components/datatable/datatable-truncated-text.svelte";
import type { TemplateResponse } from "@/types/dtos/template";
import { CaseUpper, Check, CodeXml, X } from "@lucide/svelte";

export const columns: (ColumnDef<TemplateResponse> & { sortable?: boolean })[] = [
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
        accessorKey: "replyTo",
        header: m.reply_to(),
        sortable: true,
    },
    {
        id: "templateVariants",
        accessorFn: (row) => row,
        header: m.variants(),
        cell: ({ row }) => {
            const { textRaw, htmlRaw } = row.original;

            // Determine which variant is available
            let meta;
            if (textRaw && htmlRaw) {
                meta = { text: m.html() + " & " + m.text(), color: "stroke-green-500", icon: Check };
            } else if (textRaw) {
                meta = { text: m.text(), color: "stroke-violet-500", icon: CaseUpper };
            } else if (htmlRaw) {
                meta = { text: m.html(), color: "stroke-sky-500", icon: CodeXml };
            } else {
                meta = { text: m.missing(), color: "stroke-red-500", icon: X };
            }

            return renderComponent(DatatableBadgeColored, {
                text: meta.text,
                color: meta.color,
                icon: meta.icon,
            });
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
