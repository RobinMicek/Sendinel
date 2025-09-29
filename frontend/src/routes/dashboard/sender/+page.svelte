<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { Button } from "@/components/ui/button/index.js";
    import { m } from "@/paraglide/messages";
    import { APP_NAME } from "@/config";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import DataTable from "@/components/datatable/datatable.svelte";
    import { columns } from "./columns.js";
    import type { PageResponse } from "@/types/dtos/page";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";
    import DatatableNav from "@/components/datatable/datatable-nav.svelte"
    import { userStore } from "@/stores/store-factory";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { goto } from "$app/navigation";
    import type { SenderResponse } from "@/types/dtos/sender.js";
    import SenderService from "@/services/sender-service.js";
    import DatatableSearch from "@/components/datatable/datatable-search.svelte";

    const senderService = new SenderService();

    let currentPageNumber: number
    let sortKey: string = "name"
    let sortOrder: "asc" | "desc" = "asc"
    let search: string = ""
    let pageData: PageResponse<SenderResponse>

    async function getData(pageNumber: number, sortKey: string, sortOrder: "asc" | "desc", searchString: string = "") {
        try {
            const response = await senderService.getAll(pageNumber, undefined, sortKey, sortOrder, searchString);
            pageData = response
            currentPageNumber = response.pageNumber

        } catch (e) {
            triggerAlert(m.failed_to_get_senders(), "", "error")
        }
    }

    onMount(async () => {
        await getData(currentPageNumber, sortKey, sortOrder)
    })
</script>

<div class="w-full flex flex-col gap-6">    
    <Card.Root class="w-full">
    <Card.Header>
        <Card.Title>{m.senders()}</Card.Title>
        <Card.Description>{m.services_that_app_uses_to_deliver_your_emails({app_name: APP_NAME})}</Card.Description>
        <Card.Action>
            {#if userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.SENDERS_CREATE)}
                <Button onclick={() => {goto("/dashboard/sender/new")}}>{m.new_sender()}</Button>
            {/if}
        </Card.Action>
    </Card.Header>  
    </Card.Root>

    {#if !pageData}
        <Skeleton class="aspect-video" />

    {:else}
        <DatatableSearch bind:searchString={search} getData={searchString => getData(1, sortKey, sortOrder, searchString)} />
        <DataTable
            data={pageData.content!}
            columns={columns}
            sortKey={sortKey}
            sortOrder={sortOrder}
            onSort={(key, nextOrder) => {
                sortKey = key;
                sortOrder = nextOrder;
                getData(1, sortKey, sortOrder, search);
            }}
        />
        <DatatableNav
            pageData={pageData}
            currentPageNumber={currentPageNumber}
            getData={pageNumber => getData(pageNumber, sortKey, sortOrder, search)}
        />
    {/if}

</div>