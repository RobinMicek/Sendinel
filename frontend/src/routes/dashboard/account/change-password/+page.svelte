<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { m } from "@/paraglide/messages";
    import UserService from "@/services/user-service";
    import type { UserChangePasswordRequest } from "@/types/dtos/user";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    
    const userService = new UserService()

    let isLoading = false
    let userChangePasswordRequest: UserChangePasswordRequest = {
        "password": ""
    }

    async function handleChangePassword(userChangePasswordRequest: UserChangePasswordRequest) {
        isLoading = true
        try {            
            const response = await userService.changePassword(true, userChangePasswordRequest, undefined)

            // Clear the value so it will not stay embeded in the html
            userChangePasswordRequest.password = ""
            
            triggerAlert(m.password_successfully_changed(), "", "success")
            
            goto("/dashboard")
        } catch (e) {
            triggerAlert(m.failed_to_change_password(), "", "error")
        } finally {
            isLoading = false
        }
    }
</script>


<form class="flex justify-center" on:submit|preventDefault={() => handleChangePassword(userChangePasswordRequest)}>
    {#if isLoading}
        <Skeleton class="w-full md:w-1/2 h-36" />

    {:else}
        <Card.Root class="w-full md:w-1/2">
            <Card.Header>
                <Card.Title>{m.change_password()}</Card.Title>                
            </Card.Header>
            <Card.Content>
                <div class="flex flex-col gap-6 w-full">
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
                    
                    <Button type="submit" class="w-full">{m.submit()}</Button>
                </div>
            </Card.Content>
        </Card.Root>
    {/if}
</form>