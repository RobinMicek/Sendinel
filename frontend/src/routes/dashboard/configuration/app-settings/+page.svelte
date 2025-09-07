<script lang="ts">
    import * as Card from "$lib/components/ui/card/index.js";
    import { Button } from "$lib/components/ui/button/index.js";
    import { Label } from "$lib/components/ui/label/index.js";
    import { Switch } from "$lib/components/ui/switch/index.js";
    import { m } from "@/paraglide/messages";
    import { APP_NAME } from "@/config";
    import { appSettingsStore, userStore } from "@/stores/store-factory";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import type { AppSettingsRequest, AppSettingsResponse } from "@/types/dtos/app-settings";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import AppSettingsService from "@/services/app-settings-service";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";

    const appSettingsService = new AppSettingsService()

    let isLoading = false
    let canEdit = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.APP_SETTINGS_UPDATE)
    let appSettingsData: AppSettingsResponse

    async function handleUpdate(appSettingsData: AppSettingsRequest) {
        isLoading = true
        try {
            const response = await appSettingsService.updateSettings(appSettingsData)

            // Cache the result
            appSettingsStore.set(response)

            triggerAlert(m.application_setting_successfully_updated(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_update_application_settings(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function getData() {
        isLoading = true
        try {
            const response = await appSettingsService.getSettings();
            appSettingsData = response
        } catch (e) {
            triggerAlert(m.failed_to_get_app_settings(), "", "error")
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData()
    })
</script>

<form class="w-full flex flex-col gap-6" onsubmit={() => handleUpdate(appSettingsData as AppSettingsRequest)}>
    {#if isLoading || !appSettingsData}
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
        <Card.Root>
            <Card.Header>
                <Card.Title>{m.emails()}</Card.Title>
                <Card.Description>{m.configure_the_email_sending_flow()}</Card.Description>
            </Card.Header>
            <Card.Content class="grid gap-6">
                <div class="flex gap-3">
                    <Switch class="hover:cursor-pointer" id="trackOpenedEmails" disabled={!canEdit} bind:checked={appSettingsData.trackOpenedEmails} />
                    <Label for="trackOpenedEmails">{m.track_opened_emails()}</Label>
                </div>
                <div class="flex gap-3">
                    <Switch class="hover:cursor-pointer" id="allowTemplateImport" disabled={!canEdit} bind:checked={appSettingsData.allowTemplateImports} />
                    <Label for="AllowTemplateImport">{m.allow_template_import()}</Label>                    
                </div>            
            </Card.Content>
        </Card.Root>
                    
        <Card.Root>
            <Card.Header>
                <Card.Title>{m.user_interface()}</Card.Title>
                <Card.Description>{m.configure_how_the_user_interface_looks_and_functions()}</Card.Description>
            </Card.Header>
            <Card.Content class="grid gap-6">
                <div class="flex gap-3">
                    <Switch class="hover:cursor-pointer" id="displayNewVersionAlert" disabled={!canEdit} bind:checked={appSettingsData.displayNewVersionAlert} />
                    <Label for="displayNewVersionAlert">{m.display_new_version_alert({app_name: APP_NAME})}</Label>                    
                </div>
                <div class="flex gap-3">
                    <Switch class="hover:cursor-pointer" id="useGravatar" disabled={!canEdit} bind:checked={appSettingsData.useGravatar} />
                    <Label for="useGravatar">{m.use_gravatar()}</Label>                    
                </div>
            </Card.Content>
        </Card.Root>
        

        <div class="w-full flex justify-center">
            <Button class="hover:cursor-pointer" type="submit" disabled={!canEdit}>{m.save_changes()}</Button>
        </div>
    {/if}
</form>