<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Select from "@/components/ui/select/index.js";
    import * as Table from "@/components/ui/table/index.js";
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
    import Check from "@lucide/svelte/icons/check"
    import X from "@lucide/svelte/icons/x"
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";
    import Confirm from "@/components/confirm/confirm.svelte";
    import ClientService from "@/services/client-service";
    import type { ClientRequest, ClientResponse } from "@/types/dtos/client";
    import Textarea from "@/components/ui/textarea/textarea.svelte";
    import type { SenderBasicsResponse } from "@/types/dtos/sender";
    import SenderService from "@/services/sender-service";
    import type { ClientTokenResponse } from "@/types/dtos/client-token";
    import DatatableTruncatedText from "@/components/datatable/datatable-truncated-text.svelte";
    import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";
    import CreateClientToken from "./create-client-token.svelte";
    import { senderTypesMeta } from "@/types/enums/sender-types-enum";

    export let data: { id: string }

    const clientService = new ClientService()
    const senderService = new SenderService()

    let isLoading = false
    let canEdit = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.CLIENTS_UPDATE)
    let canDelete = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.CLIENTS_DELETE)
    let canTokenCreate = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.CLIENT_TOKENS_CREATE)
    let canTokenDelete = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.CLIENT_TOKENS_DELETE)
    let senderOptions: SenderBasicsResponse[]
    let clientData: ClientResponse
    let clientTokenData: ClientTokenResponse[]
    let clientUpdateRequest: ClientRequest = {
        "name": "",
        "description": "",
        "senderId": ""
    }

    async function handleUpdate(id: string, clientUpdateRequest: ClientRequest) {
        isLoading = true
        try {            
            const response = await clientService.update(id, clientUpdateRequest)   

            triggerAlert(m.client_successfully_updated(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_update_client(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function handleDelete(id: string) {
        isLoading = true
        try {
            const response = await clientService.delete(id)    

            triggerAlert(m.client_successfully_deleted(), "", "success")
            goto("/dashboard/client")
        } catch (e) {
            triggerAlert(m.failed_to_delete_client(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function handleTokenDelete(clientId: string, tokenId: string) {
        isLoading = true
        try {
            const response = await clientService.deleteToken(clientId, tokenId)

            // Reload token data
            await getTokenData(clientId)

            triggerAlert(m.client_token_successfully_deleted(), "", "success")            
        } catch (e) {
            triggerAlert(m.failed_to_delete_client_token(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function getTokenData(id: string) {
        isLoading = true
        try {
            const clienTokenResponse = await clientService.getAllTokens(id)

            clientTokenData = clienTokenResponse
        } catch (e) {
            triggerAlert(m.failed_to_get_client(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    async function getData(id: string) {
        isLoading = true
        try {
            const clientResponse = await clientService.get(id);
            
            const senderResponse = await senderService.getAllBasics()

            senderOptions = senderResponse
            clientData = clientResponse
            
            // Set request dto
            clientUpdateRequest.name = clientResponse.name
            clientUpdateRequest.description = clientResponse.description
            clientUpdateRequest.senderId = clientResponse.sender?.id
        } catch (e) {
            triggerAlert(m.failed_to_get_client(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(data.id)
        await getTokenData(data.id)
    })
</script>

<div class="flex justify-between mb-6">
    <ReturnBack backUrl="/dashboard/client" />

    <Confirm
        disabled={!canDelete}
        triggerText={m.delete()}
        triggerVariant="destructive"
        contentText={m.do_you_really_want_to_delete_this_client()}
        action={() => {handleDelete(data.id)}}
    />
</div>

<form class="flex flex-col gap-6" on:submit={() => {handleUpdate(clientData!.id, clientUpdateRequest)}}>
    {#if isLoading || !clientData || !clientTokenData || !senderOptions}
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
        <Card.Root>
            <Card.Header>
                <Card.Title>{m.client_information()}</Card.Title>
            </Card.Header>
            <Card.Content>
                <div class="grid md:grid-cols-2 gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.name()}</Label>
                        <Input id="name" type="text" placeholder={m.acme_application()} required readonly={!canEdit} bind:value={clientUpdateRequest.name} />
                    </div>

                     <div class="flex flex-col items-start gap-2">
                        <Label for="sender">{m.sender()}</Label>
                        <Select.Root                
                            type="single"
                            disabled={!canEdit}
                            value={clientUpdateRequest.senderId}
                            onValueChange={value => clientUpdateRequest.senderId = value as string}
                        >
                            <Select.Trigger class="w-full hover:cursor-pointer" disabled={!canEdit}>
                                <div class="flex justify-start gap-2">
                                    {senderOptions.find(sender => sender.id === clientUpdateRequest.senderId)?.name}
                                    <span class="font-semibold italic">{senderTypesMeta[senderOptions.find(sender => sender.id === clientUpdateRequest.senderId)!.type].displayName}</span>                                
                                </div>
                            </Select.Trigger>
                            <Select.Content
                                id="sender"
                            >
                                {#each senderOptions as sender }
                                    <Select.Item class="hover:cursor-pointer" value={sender.id}>
                                        {sender.name}
                                        <span class="font-semibold italic">{senderTypesMeta[sender.type].displayName}</span>
                                    </Select.Item>
                                {/each}                                                        
                            </Select.Content>
                        </Select.Root>
                    </div>

                    <div class="flex flex-col items-start gap-2 md:col-span-2">
                        <Label for="description">{m.description()}</Label>
                        <Textarea id="description" class="h-32" placeholder={m.acme_application_description()} readonly={!canEdit} bind:value={clientUpdateRequest.description} />                        
                    </div>            
                </div>
            </Card.Content>
        </Card.Root>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.client_tokens()}</Card.Title>
                <Card.Description>{m.api_tokens_used_to_authenticate_this_client()}</Card.Description>
                <Card.Action>
                   <CreateClientToken
                        clientId={data.id}
                        disabled={!canTokenCreate}
                        triggerText={m.new_token()}                        
                        afterCreateAction={async () => {await getTokenData(data.id)}}
                    />
                </Card.Action>
            </Card.Header>
            <Card.Content>
                <Table.Root>
                    <Table.Header>
                        <Table.Row>
                            <Table.Head>{m.name()}</Table.Head>
                            <Table.Head>{m.description()}</Table.Head>
                            <Table.Head>{m.expiration()}</Table.Head>
                            <Table.Head>{m.last_used()}</Table.Head>
                            <Table.Head>{m.valid()}</Table.Head>
                            <Table.Head class="text-right"></Table.Head>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>                        
                        {#each clientTokenData.sort((a, b) => {
                            const expA = a.expiration ? new Date(a.expiration).getTime() : 0;
                            const expB = b.expiration ? new Date(b.expiration).getTime() : 0;
                            return expB - expA;
                        }) as token}
                            <Table.Row>
                                <Table.Cell class="font-medium">{token.name}</Table.Cell>
                                <Table.Cell class="truncate max-w-[100px]">
                                    <DatatableTruncatedText text={token.description} />
                                </Table.Cell>
                                <Table.Cell>{getLocalFormatedDate(token.expiration as string)}</Table.Cell>
                                <Table.Cell>{getLocalFormatedDate(token.lastUsedOn as string)}</Table.Cell>
                                <Table.Cell>                                    
                                    {#if token.expired}
                                        <DatatableBadgeColored icon={X} color="stroke-red-500" text="" />                                        
                                    {:else}
                                        <DatatableBadgeColored icon={Check} color="stroke-green-500" text="" />
                                    {/if}
                                </Table.Cell>
                                <Table.Cell class="text-right">
                                    <Confirm
                                        disabled={!canTokenDelete}
                                        triggerText={m.delete()}
                                        triggerVariant="outline"
                                        contentText={m.do_you_really_want_to_delete_this_client_token()}
                                        action={() => {handleTokenDelete(data.id, token.id)}}
                                    />
                                </Table.Cell>
                            </Table.Row>
                        {/each}
                    </Table.Body>
                </Table.Root>
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
                        <Input id="created_by" type="text" readonly value={getLocalFormatedDate(clientData?.updatedOn)} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.updated_by()}</Label>
                        <Input id="created_by" type="text" readonly value={clientData?.updatedBy?.firstname + " " + clientData?.updatedBy?.lastname} />
                    </div>
            
                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.created_on()}</Label>
                        <Input id="created_by" type="text" readonly value={getLocalFormatedDate(clientData?.createdOn)} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_by">{m.created_by()}</Label>
                        <Input id="created_by" type="text" readonly value={clientData?.createdBy?.firstname + " " + clientData?.createdBy?.lastname} />
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