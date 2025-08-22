<script lang="ts" generics="TData, TValue">
    import * as Card from "$lib/components/ui/card/index.js";
    import { type ColumnDef, getCoreRowModel } from "@tanstack/table-core";
    import { createSvelteTable, FlexRender } from "@/components/ui/data-table/index.js";
    import * as Table from "@/components/ui/table/index.js";
    import ChevronUp from "@lucide/svelte/icons/chevron-up";
    import ChevronDown from "@lucide/svelte/icons/chevron-down";
    import { m } from "@/paraglide/messages";

    // Extend ColumnDef to allow 'sortable' property
    type SortableColumnDef<TData, TValue = unknown> = ColumnDef<TData, TValue> & { sortable?: boolean };

    type DataTableProps<TData, TValue> = {
        columns: SortableColumnDef<TData, TValue>[];
        data: TData[];
        sortKey?: string;
        sortOrder?: "asc" | "desc";
        onSort: (key: string, nextOrder: "asc" | "desc") => void;
    };

    let { data, columns, sortKey = "", sortOrder = "asc", onSort }: DataTableProps<TData, TValue> = $props();

    const table = createSvelteTable({
        get data() {
            return data;
        },
        columns,
        getCoreRowModel: getCoreRowModel(),
    });
</script>

<Card.Root class="w-full">
    <Card.Content>
        <Table.Root>
            <Table.Header>
                {#each table.getHeaderGroups() as headerGroup (headerGroup.id)}

                    <Table.Row class="">
                        {#each headerGroup.headers as header (header.id)}

                            <Table.Head
                                class="font-semibold cursor-pointer select-none"
                                colspan={header.colSpan}
                                onclick={() => {
                                    const colDef = header.column.columnDef as SortableColumnDef<TData, TValue>;
                                    if (colDef.sortable) {
                                        const key = header.column.id ?? (colDef as any)["accessorKey"];
                                        let nextOrder: "asc" | "desc" = sortKey === key ? (sortOrder === "asc" ? "desc" : "asc") : "asc";
                                        onSort(key, nextOrder);
                                    }
                                }}
                            >
                                {#if !header.isPlaceholder}
                                    <span class="inline-flex items-center gap-1">
                                        <FlexRender
                                            content={header.column.columnDef.header}
                                            context={header.getContext()}
                                        />
                                        {#if (header.column.columnDef as SortableColumnDef<TData, TValue>).sortable && (sortKey === (header.column.id ?? (header.column.columnDef as any)["accessorKey"]))}
                                            {#if sortOrder === "asc"}
                                                <ChevronUp class="w-4 h-4" />
                                            {:else}
                                                <ChevronDown class="w-4 h-4" />
                                            {/if}
                                        {/if}
                                    </span>
                                {/if}
                            </Table.Head>
                    {/each}
                    </Table.Row>

                {/each}
            </Table.Header>

            <Table.Body>
                {#each table.getRowModel().rows as row (row.id)}
                    <Table.Row data-state={row.getIsSelected() && "selected"}>
                        {#each row.getVisibleCells() as cell (cell.id)}
                            <Table.Cell>
                                <FlexRender
                                    content={cell.column.columnDef.cell}
                                    context={cell.getContext()}
                                />
                            </Table.Cell>
                        {/each}
                    </Table.Row>
                {:else}
                    <Table.Row>
                        <Table.Cell colspan={columns.length} class="h-24 text-center">{m.no_results()}</Table.Cell>
                    </Table.Row>
                {/each}
            </Table.Body>
        </Table.Root>
    </Card.Content>
</Card.Root>