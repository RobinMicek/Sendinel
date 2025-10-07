<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Select from "$lib/components/ui/select/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import SenderService from "@/services/sender-service";
    import type { SenderRequest } from "@/types/dtos/sender";
    import { createEmptySenderConfiguration, SenderTypesEnum, senderTypesMeta } from "@/types/enums/sender-types-enum";
    import Textarea from "@/components/ui/textarea/textarea.svelte";

    const senderService = new SenderService()

    let isLoading = false
    let senderCreateRequest: SenderRequest = {
        "name": "",
        "description": "",
        "type": SenderTypesEnum.SMTP,
        "configuration": createEmptySenderConfiguration(senderTypesMeta[SenderTypesEnum.SMTP].configuration)
    }

    async function handleCreateSender(senderCreateRequest: SenderRequest) {
        isLoading = true
        try {
            const response = await senderService.create(senderCreateRequest)

            await goto("/dashboard/sender/" + response.id)
        } catch (e) {        
            triggerAlert(m.failed_to_create_sender(), "", "error")
        } finally {
            isLoading = false
        }
    }

</script>

<ReturnBack backUrl="/dashboard/sender" />

<form class="flex justify-center" on:submit={() => {handleCreateSender(senderCreateRequest)}}>
    {#if isLoading}
        <Skeleton class="w-full md:w-1/2 h-128" />

    {:else}
        <Card.Root class="w-full md:w-1/2">
            <Card.Header>
                <Card.Title>{m.new_sender()}</Card.Title>
                <Card.Description>{m.create_new_sender()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <div class="flex flex-col gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.name()}</Label>
                        <Input id="name" type="text" placeholder="Acme SendGrid" required bind:value={senderCreateRequest.name} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="type">{m.type()}</Label>
                        <Select.Root                
                            type="single"
                            value={senderCreateRequest.type}
                            onValueChange={value => senderCreateRequest.type = value as SenderTypesEnum}
                        >
                            <Select.Trigger class="w-full hover:cursor-pointer">{senderTypesMeta[senderCreateRequest.type].displayName}</Select.Trigger>
                            <Select.Content
                                id="type"                                
                            >
                                {#each Object.values(SenderTypesEnum) as type}
                                    <Select.Item class="hover:cursor-pointer" value={type}>{senderTypesMeta[type].displayName}</Select.Item>
                                {/each}                                
                            </Select.Content>
                        </Select.Root>
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="description">{m.description()}</Label>
                        <Textarea id="description" class="h-32" placeholder={m.acme_production_sendgrid_account()} bind:value={senderCreateRequest.description} />                        
                    </div>

                    <Button type="submit" class="w-full">{m.submit()}</Button>
                </div>
            </Card.Content>
        </Card.Root>
    {/if}
</form>