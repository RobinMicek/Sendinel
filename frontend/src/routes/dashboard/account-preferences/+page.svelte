<script lang="ts">
    import * as Select from "@/components/ui/select";
    import * as Card from "$lib/components/ui/card/index.js";
    import * as Alert from "$lib/components/ui/alert/index.js";
    import { Button } from "$lib/components/ui/button/index.js";
    import { m } from "@/paraglide/messages";
    import { setLocale, getLocale, locales } from "@/paraglide/runtime"
    import type { Locale} from "@/paraglide/runtime"
    import { APP_NAME } from "@/config";
    import { triggerAlert } from "@/stores/alert-store";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import { mode, toggleMode } from "mode-watcher";
    import Moon from "@lucide/svelte/icons/moon";
    import Sun from "@lucide/svelte/icons/sun";
    import Globe from "@lucide/svelte/icons/globe";
    import Info from "@lucide/svelte/icons/info";
    import Confirm from "@/components/confirm/confirm.svelte";
    import AuthService from "@/services/auth-service";
    import { tokenStore, userStore } from "@/stores/store-factory";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { goto } from "$app/navigation";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";

    const authService = new AuthService()

    let isLoading = false

    let canDeleteTotp = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.AUTH_TOTP_DELETE)
    async function handleDeleteTotp() {
        isLoading = true
        try {
            const response = await authService.totpDelete()

            // Remove token and user info to logout
            userStore.remove()
            tokenStore.remove()

            triggerAlert(m.totp_successfully_deleted(), "", "success")
            goto("/")
        } catch (e) {
            triggerAlert(m.failed_to_delete_totp(), "", "error")
        } finally {
            isLoading = false
        }
    }
</script>

<ReturnBack backUrl="/dashboard" />

<div class="w-full flex flex-col gap-6">
    {#if isLoading}
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
        <Alert.Root>
            <Info />
            <Alert.Title>{m.heads_up()}</Alert.Title>
            <Alert.Description>{m.account_preferences_are_currently_stored_in_browser({app_name: APP_NAME})}</Alert.Description>
        </Alert.Root>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.account_preferences()}</Card.Title>
                <Card.Description>{m.personalize_your_experience()}</Card.Description>
            </Card.Header>
            <Card.Content class="grid md:grid-cols-4 gap-6">    
                    <!-- Light/Dark mode toggle -->
                    <div class="w-full">
                        <Button class="hover:cursor-pointer w-full" onclick={toggleMode}>
                            {#if mode.current == "light"} 
                                <Moon />
                                {m.switch_to_dark()}
                        
                            {:else}
                                <Sun />
                                {m.switch_to_light()}
                            {/if}
                        </Button>
                    </div>
                
                    <!-- Locale select-->                
                    <div class="w-full">
                        <Select.Root                                                
                            type="single"
                            value={getLocale()}
                            onValueChange={(value) => {setLocale(value as Locale)}}
                        >
                            <Select.Trigger class="hover:cursor-pointer w-full">
                                <Globe />
                                {m.prefered_language()}
                            </Select.Trigger>
                            
                            <Select.Content id="locale-select">
                                {#each locales as locale}
                                    <Select.Item class="uppercase" value={locale}>{locale}</Select.Item>
                                {/each}
                            </Select.Content>
                        </Select.Root>
                    </div>

                    <!-- Delete TOTP -->
                    <div class="w-full">
                        <Confirm                        
                            triggerVariant="destructive"
                            triggerText={m.delete_totp()}
                            contentText={m.do_you_really_want_to_delete_your_totp()}
                            fullWidth={true}
                            disabled={!canDeleteTotp}
                            action={handleDeleteTotp}
                        />
                    </div>
            </Card.Content>
        </Card.Root>
    {/if}
</div>