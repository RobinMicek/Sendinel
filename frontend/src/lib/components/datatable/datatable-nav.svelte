<script lang="ts">
    import * as Select from "@/components/ui/select";
    import { Button } from "@/components/ui/button";
    import ArrowRight from "@lucide/svelte/icons/arrow-right";
    import ArrowRightToLine from "@lucide/svelte/icons/arrow-right-to-line";
    import ArrowLeft from "@lucide/svelte/icons/arrow-left";
    import ArrowLeftToLine from "@lucide/svelte/icons/arrow-left-to-line";
    import { preferedDatatablePageSizeStore } from "@/stores/store-factory";
    import type { PageResponse } from "@/types/dtos/page";
    import { m } from "@/paraglide/messages";

    export let pageData: PageResponse<unknown>;

    export let currentPageNumber: number;
    export let getData: (pageNumber: number) => void;

    let pageSize: number = preferedDatatablePageSizeStore.get()!;
    let selectedSize = pageSize;

    function updatePageSize(value: number) {
        selectedSize = value;
        preferedDatatablePageSizeStore.set(value);
        getData(1);
    }
</script>

<div class="w-full flex justify-between items-center">
    <!-- Page size selector -->
    <Select.Root
        type="single"
        value={String(selectedSize)}
        onValueChange={(value) => updatePageSize(Number(value))}
    >
        <Select.Trigger class="hover:cursor-pointer">{m.page_size({page_size: selectedSize})}</Select.Trigger>
        <Select.Content>
            <Select.Item class="hover:cursor-pointer" value="5">5</Select.Item>
            <Select.Item class="hover:cursor-pointer" value="10">10</Select.Item>
            <Select.Item class="hover:cursor-pointer" value="25">25</Select.Item>
            <Select.Item class="hover:cursor-pointer" value="50">50</Select.Item>
            <Select.Item class="hover:cursor-pointer" value="100">100</Select.Item>
        </Select.Content>
    </Select.Root>

  <!-- Pagination buttons -->
  <div class="flex gap-2 items-center">
        <Button
            class="hover:cursor-pointer"
            variant="ghost"
            onclick={() => getData(1)}
            disabled={pageData.first}
        >
            <ArrowLeftToLine />
        </Button>

        <Button
            class="hover:cursor-pointer"
            variant="outline"
            onclick={() => getData(currentPageNumber - 1)}
            disabled={pageData.first}
        >
            <ArrowLeft />
        </Button>

        <Button variant="secondary" class="hover:bg-secondary">
            {pageData.pageNumber} / {pageData.totalPages}
        </Button>

        <Button
            class="hover:cursor-pointer"
            variant="outline"
            onclick={() => getData(currentPageNumber + 1)}
            disabled={pageData.last}
        >
            <ArrowRight />
        </Button>

        <Button
            class="hover:cursor-pointer"
            variant="ghost"
            onclick={() => getData(pageData.totalPages)}
            disabled={pageData.last}
        >
            <ArrowRightToLine />
        </Button>
  </div>
</div>
