<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Select from "$lib/components/ui/select/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { m } from "@/paraglide/messages";
    import { UserRolesEnum, userRolesMeta } from "@/types/enums/user-roles-enum";
    import UserService from "@/services/user-service";
    import type { UserCreateRequest } from "@/types/dtos/user";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import ReturnBack from "@/components/return-back/return-back.svelte";

    const userService = new UserService()

    let isLoading = false
    let userCreateRequest: UserCreateRequest = {
        "firstname": "",
        "lastname": "",
        "email": "",
        "role": UserRolesEnum.USER,
        "password": ""
    }

    async function handleCreateUser(userCreateRequest: UserCreateRequest) {
        isLoading = true
        try {
            const response = await userService.create(userCreateRequest)

            goto("/dashboard/user/" + response.id)
        } catch (e) {        
            triggerAlert(m.failed_to_create_new_user_account(), "", "error")
        } finally {
            isLoading = false
        }
    }

</script>

<ReturnBack backUrl="/dashboard/user" />

<form class="flex justify-center" on:submit={() => {handleCreateUser(userCreateRequest)}}>
    {#if isLoading}
        <Skeleton class="w-full md:w-1/2 h-128" />

    {:else}
        <Card.Root class="w-full md:w-1/2">
            <Card.Header>
                <Card.Title>{m.new_user()}</Card.Title>
                <Card.Description>{m.create_new_user_account()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <div class="flex flex-col gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="firstname">{m.firstname()}</Label>
                        <Input id="firstname" type="text" placeholder="John" required bind:value={userCreateRequest.firstname} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="lastname">{m.lastname()}</Label>
                        <Input id="lastname" type="text" placeholder="Doe" required bind:value={userCreateRequest.lastname} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="email">{m.email_address()}</Label>
                        <Input id="email" type="email" placeholder="john.doe@sendinel.cz" required bind:value={userCreateRequest.email} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="role">{m.role()}</Label>
                        <Select.Root                
                            type="single"
                            value={userCreateRequest.role}
                            onValueChange={value => userCreateRequest.role = value as UserRolesEnum}
                        >
                            <Select.Trigger class="w-full hover:cursor-pointer">{userRolesMeta[userCreateRequest.role].translation}</Select.Trigger>
                            <Select.Content
                                id="role"
                            >
                                <Select.Item class="hover:cursor-pointer" value={UserRolesEnum.USER}>{userRolesMeta[UserRolesEnum.USER].translation}</Select.Item>
                                <Select.Item class="hover:cursor-pointer" value={UserRolesEnum.ADMIN}>{userRolesMeta[UserRolesEnum.ADMIN].translation}</Select.Item>
                            </Select.Content>
                        </Select.Root>
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="password">{m.password()}</Label>
                        <Input id="password" type="password" placeholder="*******" minlength={5} required bind:value={userCreateRequest.password} />
                    </div>

                    <Button type="submit" class="w-full">{m.submit()}</Button>
                </div>
            </Card.Content>
        </Card.Root>
    {/if}
</form>