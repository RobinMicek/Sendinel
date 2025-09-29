<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog/index.js";
    import { m } from "@/paraglide/messages";
    import Button from "@/components/ui/button/button.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import Input from "@/components/ui/input/input.svelte";    
    import { triggerAlert } from "@/stores/alert-store";
    import Loading from "@/components/loading/loading.svelte";
    import UserService from "@/services/user-service";
    import type { UserChangePasswordRequest } from "@/types/dtos/user";

    export let userId: string
    export let triggerText: string
    export let disabled: boolean = false
    export let fullWidth: boolean = false

    const userService = new UserService()

    let isLoading = false
    let userChangePasswordRequest: UserChangePasswordRequest = {
        "password": ""
    }

    async function handleChangePassword(id: string, userChangePasswordRequest: UserChangePasswordRequest) {
        isLoading = true
        try {            
            const response = await userService.changePassword(false, userChangePasswordRequest, id)

            // Clear the value so it will not stay embeded in the html
            userChangePasswordRequest.password = ""
            
            triggerAlert(m.password_successfully_changed(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_change_password(), "", "error")
        } finally {
            isLoading = false
        }
    }
</script>

<AlertDialog.Root>
    <AlertDialog.Trigger class={`${fullWidth ? "w-full" : ""}`} disabled={disabled}>        
        <Button
            variant="outline"
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
                {m.change_password()}
            </AlertDialog.Title>
        </AlertDialog.Header>

        <AlertDialog.Description>
            {#if isLoading}
                <div class="flex justify-center py-8">
                    <Loading />
                </div>
            {:else}
                <form class="flex flex-col gap-6 w-full" on:submit|preventDefault={() => handleChangePassword(userId, userChangePasswordRequest)}>
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.new_password()}</Label>
                        <Input 
                            id="new-password" 
                            type="password"
                            minlength={5} 
                            placeholder="********" 
                            required 
                            bind:value={userChangePasswordRequest.password} 
                        />
                    </div>
                    
                    <div class="flex justify-end gap-2 mt-4">
                        <AlertDialog.Cancel type="button" class="hover:cursor-pointer">{m.cancel()}</AlertDialog.Cancel>
                        <Button type="submit" class="hover: cursor-pointer">
                            {m.submit()}
                        </Button>
                    </div>
                </form>
            {/if}
        </AlertDialog.Description>
    </AlertDialog.Content>
</AlertDialog.Root>
