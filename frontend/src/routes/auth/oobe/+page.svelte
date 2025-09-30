<script lang="ts">
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import {Label} from "@/components/ui/label";
    import Loading from "@/components/loading/loading.svelte";
    import { m } from "@/paraglide/messages";
    import AuthService from "@/services/auth-service";
    import type { UserCreateRequest } from "@/types/dtos/user";
    import { goto } from "$app/navigation";
    import { triggerAlert } from "@/stores/alert-store";
    import { oobeStatusStore } from "@/stores/store-factory";
    import { APP_NAME } from "@/config";
    import { UserRolesEnum } from "@/types/enums/user-roles-enum";

    const authService = new AuthService()

    let isLoading = false

    let oobeRequest: UserCreateRequest = {
        "firstname": "",
        "lastname": "",
        "email": "",
        "password": "",
        "role": UserRolesEnum.ADMIN // This is required, but ignored and always set as ADMIN on the backend
    }

    async function handleOobe(oobeRequest: UserCreateRequest) {
        isLoading = true
        try {
            const response = await authService.oobeCreateUser(oobeRequest)

            // Change the cached value
            oobeStatusStore.set(false)

            triggerAlert(m.first_user_successfully_created(), "", "success")
            goto("/auth")
        } catch (e) {
            triggerAlert(m.failed_to_create_first_user(), "", "error")
            isLoading = false
        }
    }
</script>

<div class="flex items-center justify-center p-10">
    {#if isLoading}
        <Loading />

    {:else}
        <form class="flex flex-col items-center justify-between w-full h-full" on:submit={() => handleOobe(oobeRequest)}>
            <h1 class="text-2xl font-semibold text-center">{m.welcome_to_app({app_name: APP_NAME})}</h1>

            <p class="text-center opacity-50">{m.start_by_creating_admin_account()}</p>

            <div class="flex flex-col gap-6 w-full">
                <div class="flex flex-col items-start gap-2">
                    <Label for="firstname">{m.firstname()}</Label>
                    <Input id="firstname" type="text" placeholder="John" required bind:value={oobeRequest.firstname} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="lastname">{m.lastname()}</Label>
                    <Input id="lastname" type="texts" placeholder="Doe" required bind:value={oobeRequest.lastname} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="email">{m.email_address()}</Label>
                    <Input id="email" type="email" placeholder="john.doe@sendinel.cz" required bind:value={oobeRequest.email} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="password">{m.password()}</Label>
                    <Input id="password" type="password" placeholder="*******" minlength={5} required bind:value={oobeRequest.password} />
                </div>
            </div>

            <Button type="submit" class="w-full">Log In</Button>
        </form>
        
    {/if}
</div>