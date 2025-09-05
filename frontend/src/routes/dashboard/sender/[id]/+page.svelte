<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Select from "@/components/ui/select/index.js";
    import * as Alert from "$lib/components/ui/alert/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import { userStore } from "@/stores/store-factory";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { getLocalFormatedDate } from "@/utils/date-util";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";
    import Confirm from "@/components/confirm/confirm.svelte";
    import Textarea from "@/components/ui/textarea/textarea.svelte";
    import type { SenderRequest, SenderResponse } from "@/types/dtos/sender";
    import SenderService from "@/services/sender-service";
    import { isSenderConfigurationComplete, mergeSenderConfiguration, SenderTypesEnum, senderTypesMeta, type SenderConfigurationFieldType } from "@/types/enums/sender-types-enum";
    import Switch from "@/components/ui/switch/switch.svelte";
    import { APP_NAME } from "@/config";
    import { Info } from "@lucide/svelte";

    export let data: { id: string }

    const senderService = new SenderService()

    let isLoading = false
    let isInvalidConfigurationSchema = false
    let canEdit = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.SENDERS_UPDATE)
    let canDelete = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.SENDERS_DELETE)
    let senderData: SenderResponse

    async function handleUpdate(id: string, senderUpdateRequest: SenderRequest) {
        isLoading = true
        try {            
            const response = await senderService.update(id, senderUpdateRequest)   

            triggerAlert(m.sender_successfully_updated(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_update_sender(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function handleDelete(id: string) {
        isLoading = true
        try {
            const response = await senderService.delete(id)    

            triggerAlert(m.sender_successfully_deleted(), "", "success")
            goto("/dashboard/sender")
        } catch (e) {
            triggerAlert(m.failed_to_delete_sender(), "", "error")
        } finally {
            isLoading = false
        }
    }

    function migrateToValidConfigurationSchema(senderType: SenderTypesEnum, configuration: Record<string, SenderConfigurationFieldType>) {
        try {
            senderData.configuration =  mergeSenderConfiguration(senderTypesMeta[senderType].configuration, configuration)
            isInvalidConfigurationSchema = false

            triggerAlert(m.configuration_schema_successfully_migrated(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_migrate_configuration_schema(), "", "error")
        }
    }

    async function getData(id: string) {
        isLoading = true
        try {
            const response = await senderService.get(id)

            senderData = response

            // Check if configuration schema is correct
            isInvalidConfigurationSchema = !isSenderConfigurationComplete(senderTypesMeta[senderData.type].configuration, senderData.configuration)
        } catch (e) {
            triggerAlert(m.failed_to_get_sender(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(data.id)
    })
</script>

<div class="flex justify-between mb-6">
    <ReturnBack backUrl="/dashboard/sender" />

    <Confirm
        disabled={!canDelete}
        triggerText={m.delete()}
        triggerVariant="destructive"
        contentText={m.do_you_really_want_to_delete_this_sender()}
        action={() => {handleDelete(data.id)}}
    />
</div>

<form class="flex flex-col gap-6" on:submit={() => {handleUpdate(senderData!.id, senderData)}}>
    {#if isLoading || !senderData}
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
        <Card.Root>
            <Card.Header>
                <Card.Title>{m.sender_information()}</Card.Title>
            </Card.Header>
            <Card.Content>
                <div class="grid md:grid-cols-2 gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.name()}</Label>
                        <Input id="name" type="text" placeholder="Acme SendGrid" required readonly={!canEdit} bind:value={senderData.name} />
                    </div>

                     <div class="flex flex-col items-start gap-2">
                        <Label for="type">{m.type()}</Label>
                        <Select.Root                
                            type="single"
                            value={senderData.type}
                            onValueChange={value => senderData.type = value as SenderTypesEnum}
                        >
                            <Select.Trigger class="w-full hover:cursor-pointer">{senderTypesMeta[senderData.type].displayName}</Select.Trigger>
                            <Select.Content
                                id="type"                                
                            >
                                {#each Object.values(SenderTypesEnum) as type}
                                    <Select.Item class="hover:cursor-pointer" value={type}>{senderTypesMeta[type].displayName}</Select.Item>
                                {/each}                                
                            </Select.Content>
                        </Select.Root>
                    </div>

                    <div class="flex flex-col items-start gap-2 md:col-span-2">
                        <Label for="description">{m.description()}</Label>
                        <Textarea id="description" class="h-32" placeholder={m.acme_production_sendgrid_account()} readonly={!canEdit} bind:value={senderData.description} />                        
                    </div>            
                </div>
            </Card.Content>
        </Card.Root>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.configuration()}</Card.Title>
            </Card.Header>
            <Card.Content>
                {#if isInvalidConfigurationSchema}
                    <div class="flex flex-col justify-center gap-6">
                        <Alert.Root>
                            <Info />
                            <Alert.Title>{m.heads_up()}</Alert.Title>
                            <Alert.Description>{m.your_configuration_schema_is_invalid({app_name: APP_NAME})}</Alert.Description>
                        </Alert.Root>
                        
                        <Button class="hover:cursor-pointer" onclick={() => migrateToValidConfigurationSchema(senderData.type, senderData.configuration)}>{m.migrate()}</Button>
                    </div>

                {:else}
                    <div class="flex flex-col gap-6">            
                        <div class="grid md:grid-cols-2 gap-6 w-full">
                            {#each Object.entries(senderTypesMeta[senderData.type].configuration).filter(([_, field]) => typeof field.type !== "boolean") as [fieldName, fieldParams]}
                                <div class="flex flex-col items-start gap-2">
                                    <Label for={fieldName}>{fieldParams.translation}</Label>
                                    <Input 
                                        id={fieldName} 
                                        type={
                                            typeof fieldParams.type === 'number'
                                                ? 'number'
                                                : fieldParams.isSensitive
                                                    ? 'password'
                                                    : 'text'
                                        } 
                                        required={fieldParams.required} 
                                        readonly={!canEdit} 
                                        bind:value={senderData.configuration[fieldName]} />
                                </div>
                            {/each}
                        </div>

                        <div class="flex flex-col gap-6 w-full">
                            {#each Object.entries(senderTypesMeta[senderData.type].configuration).filter(([_, field]) => typeof field.type == "boolean") as [fieldName, fieldParams]}
                                <div class="flex gap-3">
                                    <Switch id={fieldName} required={fieldParams.required} disabled={!canEdit} bind:checked={senderData.configuration[fieldName] as boolean} />
                                    <Label for={fieldName}>{fieldParams.translation}</Label>
                                </div>
                            {/each}
                        </div>
                </div>
                {/if}            
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
                        <Input id="created_by" type="text" readonly value={getLocalFormatedDate(senderData?.updatedOn)} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.updated_by()}</Label>
                        <Input id="created_by" type="text" readonly value={senderData?.updatedBy?.firstname + " " + senderData?.updatedBy?.lastname} />
                    </div>
            
                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.created_on()}</Label>
                        <Input id="created_by" type="text" readonly value={getLocalFormatedDate(senderData?.createdOn)} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.created_by()}</Label>
                        <Input id="created_by" type="text" readonly value={senderData?.createdBy?.firstname + " " + senderData?.createdBy?.lastname} />
                    </div>
                </div>
            </Card.Content>
        </Card.Root>

        <div class="w-full flex justify-center gap-6">
            <div></div>
            <Button class="hover:cursor-pointer" type="submit" disabled={!canEdit || isInvalidConfigurationSchema}>{m.save_changes()}</Button>
        </div>
    {/if}
</form>