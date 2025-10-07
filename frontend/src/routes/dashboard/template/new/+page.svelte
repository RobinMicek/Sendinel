<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { Switch } from "$lib/components/ui/switch/index.js";
    import { Textarea } from "$lib/components/ui/textarea/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { goto } from "$app/navigation";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import TemplateService from "@/services/template-service";
    import type { TemplateRequest } from "@/types/dtos/template";
    import { SAMPLE_TEMPLATE } from "@/config";

    const templateService = new TemplateService()

    let isLoading = false
    let generateExampleTemplate = false
    let templateCreateRequest: TemplateRequest = {
        "name": "",
        "description": "",
        "replyTo": "",
        "subject": "",
        "schema": {"type": "object", "properties": {}},
        "textRaw": "",
        "htmlRaw": "",
        "tags": []
    }

    async function handleCreateTemplate(templateCreateRequest: TemplateRequest, generateExampleTemplate: boolean) {
        isLoading = true
        try {
            if (generateExampleTemplate) {
                templateCreateRequest.subject = SAMPLE_TEMPLATE.subject,
                templateCreateRequest.schema = SAMPLE_TEMPLATE.schema,
                templateCreateRequest.textRaw = SAMPLE_TEMPLATE.textRaw,
                templateCreateRequest.htmlRaw = SAMPLE_TEMPLATE.htmlRaw
            }

            const response = await templateService.create(templateCreateRequest)

            await goto("/dashboard/template/" + response.id)
        } catch (e) {        
            triggerAlert(m.failed_to_create_new_template(), "", "error")
        } finally {
            isLoading = false
        }
    }

</script>

<ReturnBack backUrl="/dashboard/template" />

<form class="flex justify-center" on:submit={() => {handleCreateTemplate(templateCreateRequest, generateExampleTemplate)}}>
    {#if isLoading}
        <Skeleton class="w-full md:w-1/2 h-128" />

    {:else}
        <Card.Root class="w-full md:w-1/2">
            <Card.Header>
                <Card.Title>{m.new_template()}</Card.Title>
                <Card.Description>{m.create_new_template()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <div class="flex flex-col gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="name">{m.name()}</Label>
                        <Input id="name" type="text" placeholder={m.order_confirmation()} required bind:value={templateCreateRequest.name} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="reply-to">{m.reply_to()}</Label>
                        <Input id="reply-to" type="email" placeholder="no-reply@acme.com" required bind:value={templateCreateRequest.replyTo} />
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="description">{m.description()}</Label>
                        <Textarea id="description" class="h-32" placeholder={m.confirmation_of_new_order_with_order_id_and_list_of_purchased_items()} bind:value={templateCreateRequest.description} />                        
                    </div>

                    <div class="flex gap-3">
                        <Switch class="hover:cursor-pointer" id="generate-sample-template" bind:checked={generateExampleTemplate} />
                        <Label for="generate-sample-template">{m.generate_example_template()}</Label>
                    </div>
                
                    <Button type="submit" class="w-full">{m.submit()}</Button>
                </div>
            </Card.Content>
        </Card.Root>
    {/if}
</form>