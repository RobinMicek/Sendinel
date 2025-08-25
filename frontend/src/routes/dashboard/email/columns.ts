import type { ColumnDef } from "@tanstack/table-core";
import { m } from "@/paraglide/messages";
import { renderComponent, renderSnippet } from "@/components/ui/data-table";
import DatatableLink from "@/components/datatable/datatable-link.svelte";
import type { EmailResponse, EmailStatusResponse } from "@/types/dtos/email";
import { getLocalFormatedDate } from "@/utils/date-util";
import { EmailStatusesEnum, emailStatusesMeta } from "@/types/enums/email-statuses-enum";
import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";

export const columns: (ColumnDef<EmailResponse> & { sortable?: boolean })[] = [
  {
    accessorKey: "toAddress",
    header: m.email_address(),
    sortable: true,
  },
  {
    accessorKey: "template.name",
    header: m.template(),
    sortable: true,
  },
  {
    id: "requestedBy",
    accessorFn: (row) => row.requestedBy,
    header: m.requested_by(),
    sortable: true,
    cell: ({row}) => {
        return row.original.requestedBy.name
    }
  },
  {
    id: "status",
    accessorFn: (row) => row.emailStatuses, 
    header: m.status(),
    cell: ({ row }) => {
        const statuses = row.original.emailStatuses || [];
        const lastStatus = statuses.length > 0 ? statuses.at(-1)!.status : EmailStatusesEnum.UNKNOWN;
        const meta = emailStatusesMeta[lastStatus];

       return renderComponent(DatatableBadgeColored, {text: meta.translation, color: meta.color, icon: meta.icon})
    },
  },
  {
    accessorKey: "createdOn",
    header: m.created_on(),
    sortable: true,
    cell: ({ row }) => {
      return getLocalFormatedDate(row.original.createdOn)
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
