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
    import ClientService from "@/services/client-service.js";
    import type { ClientResponse } from "@/types/dtos/client.js";
    import DatatableSearch from "@/components/datatable/datatable-search.svelte";

    const clientService = new ClientService();

    let isLoading = false
    let currentPageNumber: number
    let sortKey: string = "name"
    let sortOrder: "asc" | "desc" = "asc"
    let search: string = ""
    let pageData: PageResponse<ClientResponse>

    async function getData(pageNumber: number, sortKey: string, sortOrder: "asc" | "desc", searchString: string = "") {
        isLoading = true
        try {
            const response = await clientService.getAll(pageNumber, undefined, sortKey, sortOrder, searchString);
            pageData = response
            currentPageNumber = response.pageNumber

        } catch (e) {
            triggerAlert(m.failed_to_get_clients(), "", "error")
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(currentPageNumber, sortKey, sortOrder)
    })
</script>

<div class="w-full flex flex-col gap-6">    
    <Card.Root class="w-full">
    <Card.Header>
        <Card.Title>{m.clients()}</Card.Title>
        <Card.Description>{m.your_applications_and_services_that_send_emails_using_app({app_name: APP_NAME})}</Card.Description>
        <Card.Action>
            {#if userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.CLIENTS_CREATE)}
                <Button onclick={() => {goto("/dashboard/client/new")}}>{m.new_client()}</Button>
            {/if}
        </Card.Action>
    </Card.Header>  
    </Card.Root>

    {#if !pageData || isLoading}
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