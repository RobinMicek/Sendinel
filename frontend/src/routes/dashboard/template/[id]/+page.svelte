<script lang="ts">
    import * as Tabs from "$lib/components/ui/tabs/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import { userStore } from "@/stores/store-factory";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";
    import Confirm from "@/components/confirm/confirm.svelte";
    import type { TemplateRequest, TemplateResponse } from "@/types/dtos/template";
    import TemplateService from "@/services/template-service";
    import TabGeneral from "./tabs/tab-general.svelte";

    export let data: { id: string }

    const templateService = new TemplateService()

    let isLoading = false
    let canEdit = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.TEMPLATES_UPDATE)
    let canDelete = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.TEMPLATES_DELETE)
    let templateData: TemplateResponse

    async function handleUpdate(id: string, templateUpdateRequest: TemplateRequest) {
        isLoading = true
        try {            
            const response = await templateService.update(id, templateUpdateRequest)   

            triggerAlert(m.template_successfully_updated(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_update_template(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function handleDelete(id: string) {
        isLoading = true
        try {
            const response = await templateService.delete(id)    

            triggerAlert(m.template_successfully_deleted(), "", "success")
            goto("/dashboard/template")
        } catch (e) {
            triggerAlert(m.failed_to_delete_template(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function getData(id: string) {
        isLoading = true
        try {
            const response = await templateService.get(id)

            templateData = response
        } catch (e) {
            triggerAlert(m.failed_to_get_template(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(data.id)
    })
</script>

<div class="flex justify-between mb-6">
    <ReturnBack backUrl="/dashboard/template" />

    <Confirm
        disabled={!canDelete}
        triggerText={m.delete()}
        triggerVariant="destructive"
        contentText={m.do_you_really_want_to_delete_this_template()}
        action={() => {handleDelete(data.id)}}
    />
</div>

<form class="flex flex-col gap-6" on:submit={() => {handleUpdate(templateData!.id, templateData)}}>
    {#if isLoading || !templateData}
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
        <Tabs.Root value="general">
            <Tabs.List>
                <Tabs.Trigger class="hover:cursor-pointer" value="general">{m.general()}</Tabs.Trigger>
                <Tabs.Trigger class="hover:cursor-pointer" value="tab">Tab</Tabs.Trigger>
            </Tabs.List>
            <Tabs.Content value="general">
                <TabGeneral canEdit={canEdit} bind:templateData={templateData} />
            </Tabs.Content>

            <Tabs.Content value="tab">There will be something.</Tabs.Content>
        </Tabs.Root>

        <div class="w-full flex justify-center gap-6">
            <div></div>
            <Button class="hover:cursor-pointer" type="submit" disabled={!canEdit}>{m.save_changes()}</Button>
        </div>
    {/if}
</form>