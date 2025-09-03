<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Select from "@/components/ui/select/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import { userStore } from "@/stores/store-factory";
    import type { UserResponse, UserUpdateRequest } from "@/types/dtos/user";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { hasPermission, UserRolesEnum, userRolesMeta } from "@/types/enums/user-roles-enum";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { getLocalFormatedDate } from "@/utils/date-util";
    import UserService from "@/services/user-service";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";
    import Confirm from "@/components/confirm/confirm.svelte";

    export let data: { id: string }

    const userService = new UserService()

    let isLoading = false
    let canEdit = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.USERS_UPDATE)
    let canDelete = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.USERS_DELETE) && userStore.get()?.id != data.id
    let userData: UserResponse

    async function handleUpdate(id: string, userUpdateRequest: UserUpdateRequest) {
        isLoading = true
        try {
            const response = await userService.update(id, userUpdateRequest)    
            
            // Update user data
            userData = response

            triggerAlert(m.user_successfully_updated(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_update_user(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function handleDelete(id: string) {
        isLoading = true
        try {
            const response = await userService.delete(id)    

            triggerAlert(m.user_successfully_deleted(), "", "success")
            goto("/dashboard/user")
        } catch (e) {
            triggerAlert(m.failed_to_delete_user(), "", "error")
        } finally {
            isLoading = false
        }
    } 

    async function getData(id: string) {
        isLoading = true
        try {
            const response = await userService.get(id);
            userData = response
        } catch (e) {
            triggerAlert(m.failed_to_get_user(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(data.id)
    })
</script>

<div class="flex justify-between mb-6">
    <ReturnBack backUrl="/dashboard/user" />

    <Confirm
        disabled={!canDelete}
        triggerText={m.delete()}
        triggerVariant="destructive"
        contentText={m.do_you_really_want_to_delete_this_user()}
        action={() => {handleDelete(data.id)}}
    />
</div>

<form class="flex flex-col gap-6" on:submit={() => {handleUpdate(userData!.id, userData)}}>
    {#if isLoading || !userData}
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
        <Card.Root>
            <Card.Header>
                <Card.Title>{m.user_information()}</Card.Title>
            </Card.Header>
            <Card.Content>
                <div class="grid md:grid-cols-2 gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="firstname">{m.firstname()}</Label>
                        <Input id="firstname" type="text" placeholder="John" required readonly={!canEdit} bind:value={userData.firstname} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="lastname">{m.lastname()}</Label>
                        <Input id="lastname" type="texts" placeholder="Doe" required readonly={!canEdit} bind:value={userData.lastname} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="email">{m.email_address()}</Label>
                        <Input id="email" type="email" placeholder="john.doe@sendinel.cz" readonly={!canEdit} required bind:value={userData.email} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="role">{m.role()}</Label>
                        <Select.Root                
                            type="single"
                            disabled={!canEdit}
                            value={userData.role}
                            onValueChange={value => userData.role = value as UserRolesEnum}
                        >
                            <Select.Trigger class="w-full hover:cursor-pointer" disabled={!canEdit}>{userRolesMeta[userData.role].translation}</Select.Trigger>
                            <Select.Content
                                id="role"
                            >
                                <Select.Item class="hover:cursor-pointer" value={UserRolesEnum.USER}>{userRolesMeta[UserRolesEnum.USER].translation}</Select.Item>
                                <Select.Item class="hover:cursor-pointer" value={UserRolesEnum.ADMIN}>{userRolesMeta[UserRolesEnum.ADMIN].translation}</Select.Item>
                            </Select.Content>
                        </Select.Root>
                    </div>
                </div>
            </Card.Content>
        </Card.Root>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.history()}</Card.Title>
            </Card.Header>
            <Card.Content>
                <div class="grid md:grid-cols-2 gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.updated_on()}</Label>
                        <Input id="created_by" type="text" readonly value={getLocalFormatedDate(userData?.updatedOn)} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.updated_by()}</Label>
                        <Input id="created_by" type="text" readonly value={userData?.updatedBy?.firstname + " " + userData?.updatedBy?.lastname} />
                    </div>
            
                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.created_on()}</Label>
                        <Input id="created_by" type="text" readonly value={getLocalFormatedDate(userData?.createdOn)} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.created_by()}</Label>
                        <Input id="created_by" type="text" readonly value={userData?.createdBy?.firstname + " " + userData?.createdBy?.lastname} />
                    </div>
                </div>
            </Card.Content>
        </Card.Root>

        <div class="w-full flex justify-center gap-6">
            <div></div>
            <Button class="hover:cursor-pointer" type="submit" disabled={!canEdit}>{m.save_changes()}</Button>
        </div>
    {/if}
</form>