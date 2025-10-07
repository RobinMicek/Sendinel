<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { Textarea } from "$lib/components/ui/textarea/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import type { ClientRequest } from "@/types/dtos/client";
    import ClientService from "@/services/client-service";

    const clientService = new ClientService()

    let isLoading = false
    let clientCreateRequest: ClientRequest = {
        "name": "",
        "description": "",
        "senderId": ""
    }

    async function handleCreateClient(clientCreateRequest: ClientRequest) {
        isLoading = true
        try {
            const response = await clientService.create(clientCreateRequest)

            await goto("/dashboard/client/" + response.id)
        } catch (e) {        
            triggerAlert(m.failed_to_create_new_client(), "", "error")
        } finally {
            isLoading = false
        }
    }

</script>

<ReturnBack backUrl="/dashboard/client" />

<form class="flex justify-center" on:submit={() => {handleCreateClient(clientCreateRequest)}}>
    {#if isLoading}
        <Skeleton class="w-full md:w-1/2 h-128" />

    {:else}
        <Card.Root class="w-full md:w-1/2">
            <Card.Header>
                <Card.Title>{m.new_client()}</Card.Title>
                <Card.Description>{m.create_new_client()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <div class="flex flex-col gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.name()}</Label>
                        <Input id="name" type="text" placeholder={m.acme_application()} required bind:value={clientCreateRequest.name} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="description">{m.description()}</Label>
                        <Textarea id="description" class="h-32" placeholder={m.acme_application_description()} bind:value={clientCreateRequest.description} />                        
                    </div>
                
                    <Button type="submit" class="w-full">{m.submit()}</Button>
                </div>
            </Card.Content>
        </Card.Root>
    {/if}
</form>