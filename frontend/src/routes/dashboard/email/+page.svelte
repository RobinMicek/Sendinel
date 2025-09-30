<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { m } from "@/paraglide/messages";
    import { APP_NAME } from "@/config";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import DataTable from "@/components/datatable/datatable.svelte";
    import { columns } from "./columns.js";
    import type { PageResponse } from "@/types/dtos/page";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";
    import DatatableNav from "@/components/datatable/datatable-nav.svelte"
    import EmailService from "@/services/email-service.js";
    import type { EmailResponse } from "@/types/dtos/email.js";
    import DatatableSearch from "@/components/datatable/datatable-search.svelte";

    const emailService = new EmailService();

    let isLoading = false
    let currentPageNumber: number
    let sortKey: string = "createdOn"
    let sortOrder: "asc" | "desc" = "desc"
    let search: string = ""
    let pageData: PageResponse<EmailResponse>

    async function getData(pageNumber: number, sortKey: string, sortOrder: "asc" | "desc", searchString: string = "") {
        isLoading = true
        try {
            const response = await emailService.getAll(pageNumber, undefined, sortKey, sortOrder, searchString);
            pageData = response
            currentPageNumber = response.pageNumber

        } catch (e) {
            triggerAlert(m.failed_to_get_emails(), "", "error")
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
        <Card.Title>{m.emails()}</Card.Title>
        <Card.Description>{m.email_sent_via_app({app_name: APP_NAME})}</Card.Description>        
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