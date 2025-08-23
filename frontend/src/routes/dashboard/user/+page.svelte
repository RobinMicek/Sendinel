<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { Button } from "@/components/ui/button/index.js";
    import { m } from "@/paraglide/messages";
    import { APP_NAME } from "@/config";
    import type { UserResponse } from "@/types/dtos/user";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import DataTable from "@/components/datatable/datatable.svelte";
    import { columns } from "./columns.js";
    import type { PageResponse } from "@/types/dtos/page";
    import { triggerAlert } from "@/stores/alert-store";
    import UserService from "@/services/user-service";
    import { onMount } from "svelte";
    import DatatableNav from "@/components/datatable/datatable-nav.svelte"
    import { userStore } from "@/stores/store-factory";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { goto } from "$app/navigation";

    const userService = new UserService();

    let currentPageNumber: number
    let sortKey: string
    let sortOrder: "asc" | "desc"
    let pageData: PageResponse<UserResponse>

    async function getData(pageNumber: number, sortKey: string, sortOrder: "asc" | "desc") {
        try {
            const response = await userService.getAll(pageNumber, undefined, sortKey, sortOrder);
            pageData = response
            currentPageNumber = response.pageNumber

        } catch (e) {
            triggerAlert("Failed to fetch users", "", "error")
        }
    }

    onMount(async () => {
        await getData(currentPageNumber, "", "asc")
    })
</script>

<div class="w-full flex flex-col gap-6">    
    <Card.Root class="w-full">
    <Card.Header>
        <Card.Title>{m.users()}</Card.Title>
        <Card.Description>{m.user_accounts_that_access_app({app_name: APP_NAME})}</Card.Description>
        <Card.Action>
            {#if userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.USERS_CREATE)}
                <Button class="hover:cursor-pointer" onclick={() => {goto("/dashboard/user/new")}}>{m.new_user()}</Button>
            {/if}
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