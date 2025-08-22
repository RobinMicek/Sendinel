<script lang="ts">
    import * as Card from "$lib/components/ui/card/index.js";
    import { Button } from "$lib/components/ui/button/index.js";
    import { m } from "@/paraglide/messages";
    import { APP_NAME } from "@/config";
    import type { UserResponse } from "@/types/dtos/user";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import DataTable from "../../../lib/components/datatable/datatable.svelte";
    import { columns } from "./columns.js";
    import type { PageResponse } from "@/types/dtos/page";
    import { triggerAlert } from "@/stores/alert-store";
    import UserService from "@/services/user-service";
    import { onMount } from "svelte";
    import DatatableNav from "@/components/datatable/datatable-nav.svelte"

    const userService = new UserService();

    let isLoading = false
    let currentPageNumber: number
    let sortKey: string
    let sortOrder: "asc" | "desc"
    let pageData: PageResponse<UserResponse>

    async function getData(pageNumber: number, sortKey: string, sortOrder: "asc" | "desc") {
        isLoading = true
        try {
            const response = await userService.getAll(pageNumber, undefined, sortKey, sortOrder);
            pageData = response
            currentPageNumber = response.pageNumber

        } catch (e) {
            triggerAlert("Failed to fetch users", "", "error");
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        getData(currentPageNumber, "", "asc")
    })
</script>

<div class="w-full flex flex-col gap-6">    
    <Card.Root class="w-full">
    <Card.Header>
        <Card.Title>{m.users()}</Card.Title>
        <Card.Description>{m.user_accounts_that_access_app({app_name: APP_NAME})}</Card.Description>
        <Card.Action>
        <Button class="hover:cursor-pointer">{m.new_user()}</Button>
        </Card.Action>
    </Card.Header>  
    </Card.Root>

    {#if !pageData}
        <Skeleton class="aspect-video" />

    {:else}
        <DataTable
            data={pageData.content!}
            columns={columns}
            sortKey={sortKey}
            sortOrder={sortOrder}
            onSort={(key, nextOrder) => {
                sortKey = key;
                sortOrder = nextOrder;
                getData(1, sortKey, sortOrder);
            }}
        />
        <DatatableNav
            pageData={pageData}
            currentPageNumber={currentPageNumber}
            getData={pageNumber => getData(pageNumber, sortKey, sortOrder)}
        />
    {/if}

</div>