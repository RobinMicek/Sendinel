<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog/index.js";
    import { m } from "@/paraglide/messages";
    import Button from "@/components/ui/button/button.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import Input from "@/components/ui/input/input.svelte";    
    import ClientService from "@/services/client-service";    
    import type { ClientTokenRequest, ClientTokenValueResponse } from "@/types/dtos/client-token";
    import { triggerAlert } from "@/stores/alert-store";
    import Loading from "@/components/loading/loading.svelte";
    import CalendarInput from "@/components/calendar/calendar-input.svelte";
    import CopyToClipboardButton from "@/components/copy-to-clipboard/copy-to-clipboard-button.svelte";

    export let clientId: string
    export let triggerText: string
    export let disabled: boolean = false
    export let afterCreateAction: () => void
    export let fullWidth: boolean = false

    const clientService = new ClientService()

    let isLoading = false
    let tokenCreated = false
    let tokenResponse: ClientTokenValueResponse | null = null
    let tokenRequest: ClientTokenRequest = {
        name: "",
        description: "",
        expiration: "2025-11-12"
    };

    async function handleCreate(id: string, tokenRequest: ClientTokenRequest) {
        isLoading = true
        try {            
            const response = await clientService.createToken(id, tokenRequest)
            tokenResponse = response
            tokenCreated = true

            triggerAlert(m.client_token_successfully_created(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_create_client_token(), "", "error")
        } finally {
            isLoading = false
        }
    }
</script>

<AlertDialog.Root>
    <AlertDialog.Trigger class={`${fullWidth ? "w-full" : ""}`} disabled={disabled}>        
        <Button
            disabled={disabled}        
            type="button"            
            class={`hover:cursor-pointer ${fullWidth ? "w-full" : ""}`}        
        >
            {triggerText}
        </Button>
    </AlertDialog.Trigger>

    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>
                {m.create_new_token()}
            </AlertDialog.Title>
        </AlertDialog.Header>

        <AlertDialog.Description>
            {#if isLoading}
                <div class="flex justify-center py-8">
                    <Loading />
                </div>
            {:else if !tokenCreated}
                <form class="flex flex-col gap-6 w-full" on:submit|preventDefault={() => handleCreate(clientId, tokenRequest)}>
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.name()}</Label>
                        <Input 
                            id="name" 
                            type="text" 
                            placeholder={m.internal_notification_service_token()} 
                            required 
                            bind:value={tokenRequest.name} 
                        />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="expiration">{m.expiration()}</Label>
                        <CalendarInput
                            id="expiration"
                            bind:value={tokenRequest.expiration}
                        />
                    </div>


                    <div class="flex flex-col items-start gap-2">
                        <Label for="description">{m.description()}</Label>
                        <Input
                            id="description"
                            placeholder={m.used_by_internal_notification_service()} 
                            bind:value={tokenRequest.description} 
                        />                        
                    </div>

                    <div class="flex justify-end gap-2 mt-4">
                        <AlertDialog.Cancel class="hover:cursor-pointer">{m.cancel()}</AlertDialog.Cancel>
                        <Button type="submit" class="hover: cursor-pointer">
                            {m.submit()}
                        </Button>
                    </div>
                </form>
            {:else}
                <div class="flex flex-col gap-4">
                    {m.please_copy_your_new_token_and_store_it_securely_you_will_not_be_able_to_see_the_token_value_after_you_close_this_window()}
                    
                    <div class="flex gap-2">
                        <Input
                            type="text" 
                            readonly 
                            value={tokenResponse?.token} 
                        />
                        <CopyToClipboardButton text={tokenResponse!.token} />
                    </div>

                    <div class="flex justify-end gap-2 mt-4">
                        <AlertDialog.Cancel class="hover:cursor-pointer" onclick={afterCreateAction}>{m.close()}</AlertDialog.Cancel>
                    </div>
                </div>
            {/if}
        </AlertDialog.Description>
    </AlertDialog.Content>
</AlertDialog.Root>
