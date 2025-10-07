<script lang="ts">
    import * as Tabs from "$lib/components/ui/tabs/index.js";
    import * as Code from '@/components/ui/code';
    import * as Card from "@/components/ui/card/index.js";
    import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { getLocalFormatedDate } from "@/utils/date-util";
    import Textarea from "@/components/ui/textarea/textarea.svelte";
    import { m } from "@/paraglide/messages";
    import type { TemplateResponse, TemplateTagResponse } from "@/types/dtos/template";
    import { TemplateRequestGenerator } from "@/utils/template-request-generator-util";
    import type { SupportedLanguage } from "@/components/ui/code/shiki";
    import CopyToClipboardButton from "@/components/copy-to-clipboard/copy-to-clipboard-button.svelte";
    import Button from "@/components/ui/button/button.svelte";
    import { ChevronDown, Plus } from "@lucide/svelte";
    import Checkbox from "@/components/ui/checkbox/checkbox.svelte";
    import type { UserBasicsResponse } from "@/types/dtos/user";

    export let canEdit: boolean | undefined
    export let templateData: TemplateResponse
    export let allTemplateTags: TemplateTagResponse[]

    let newTagName: string = ""

    const templateRequestGenerator = new TemplateRequestGenerator(templateData)

    let codeExamples = {
        "python": templateRequestGenerator.generatePython(),
        "typescript": templateRequestGenerator.generateTypeScript(),
        "bash": templateRequestGenerator.generateCurl()
    }

      $: selectedTags = templateData.tags.map(t => t.name);
</script> 

<div class="flex flex-col gap-6">
    <Card.Root>
        <Card.Header>
            <Card.Title>{m.template_information()}</Card.Title>
        </Card.Header>
        <Card.Content>
            <div class="grid md:grid-cols-2 gap-6 w-full">
                <div class="flex flex-col items-start gap-2">
                    <Label for="name">{m.name()}</Label>
                    <Input id="name" type="text" placeholder={m.order_confirmation()} required readonly={!canEdit} bind:value={templateData.name} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="reply-to">{m.reply_to()}</Label>
                    <Input id="reply-to" type="email" placeholder="no-reply@acme.com" required readonly={!canEdit} bind:value={templateData.replyTo} />
                </div>

                <div class="flex flex-col items-start gap-2 md:col-span-2">
                    <Label for="description">{m.description()}</Label>
                    <Textarea id="description" class="h-32" placeholder={m.confirmation_of_new_order_with_order_id_and_list_of_purchased_items()} readonly={!canEdit} bind:value={templateData.description} />                        
                </div>

                <div class="flex flex-col items-start gap-2 w-full md:col-span-2">
                    <Label for="type">{m.tags()}</Label>

                    <Popover>
                        <PopoverTrigger class="w-full" disabled={!canEdit}>
                            <Button variant="outline" class="w-full justify-between">
                                {#if selectedTags.length > 0}
                                    {selectedTags.join(", ")}
                                {:else}
                                    {m.select_tags()}
                                {/if}
                                <ChevronDown />
                            </Button>
                        </PopoverTrigger>

                        <PopoverContent class="w-[var(--radix-popover-trigger-width)] p-2">
                            <div class="flex flex-col gap-1">
                                {#each allTemplateTags as tag}
                                    <label class="flex items-center gap-4 cursor-pointer w-full mb-2">
                                        <Checkbox
                                            checked={selectedTags.includes(tag.name)}
                                            onclick={() =>
                                                templateData.tags = selectedTags.includes(tag.name)
                                                ? templateData.tags.filter(x => x.name !== tag.name)
                                                : [...templateData.tags, { name: tag.name, createdBy: {} as UserBasicsResponse , createdOn: "", id: "" }]
                                            }
                                        />
                                        <span>{tag.name}</span>
                                    </label>
                                {/each}

                                <div class="flex gap-2">
                                    <Input type="text" placeholder={m.order_confirmation()} bind:value={newTagName} />
                                    <Button onclick={() => {
                                            if (newTagName.length > 0) {
                                                allTemplateTags = [...allTemplateTags, { name: newTagName, createdBy: {} as UserBasicsResponse , createdOn: "", id: "" }]
                                            }

                                            newTagName = ""
                                        }}
                                    >
                                        <Plus />
                                    </Button>
                                </div>
                            </div>
                        </PopoverContent>
                    </Popover>
                </div>
            </div>
        </Card.Content>
    </Card.Root>

    <Card.Root>
        <Card.Header>
            <Card.Title>{m.how_to_use()}</Card.Title>
            <Card.Description>{m.how_to_send_email_using_this_template()}</Card.Description>
        </Card.Header>
        <Card.Content>
            <Tabs.Root value={Object.entries(codeExamples)[0][0]}>
                <Tabs.List>
                    {#each Object.entries(codeExamples) as [key, value]}
                        <Tabs.Trigger class="hover:cursor-pointer capitalize" value={key}>{key}</Tabs.Trigger>
                    {/each}
                </Tabs.List>

                {#each Object.entries(codeExamples) as [key, value]}
                    <Tabs.Content value={key}>
                        <div class="absolute right-10 z-11">
                            <CopyToClipboardButton text={value} />
                        </div>
                        <div class="max-w-full overflow-x-auto">
                            {#if value.split(/\r\n|\r|\n/).length > 25}
                                <Code.Overflow>
                                    <Code.Root lang={key as SupportedLanguage} code={value}></Code.Root>                        
                                </Code.Overflow>
                            {:else}
                                <Code.Root lang={key as SupportedLanguage} code={value}></Code.Root>                        
                            {/if}
                        </div>
                    </Tabs.Content>
                {/each}
            </Tabs.Root>
        </Card.Content>
    </Card.Root>


    <Card.Root>
        <Card.Header>
            <Card.Title>{m.history()}</Card.Title>
        </Card.Header>
        <Card.Content>
            <div class="grid md:grid-cols-2 gap-6 w-full">
                <div class="flex flex-col items-start gap-2">
                    <Label for="updated_on">{m.updated_on()}</Label>
                    <Input id="updated_on" type="text" readonly value={getLocalFormatedDate(templateData?.updatedOn)} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="updated_by">{m.updated_by()}</Label>
                    <Input id="updated_by" type="text" readonly value={templateData?.updatedBy?.firstname + " " + templateData?.updatedBy?.lastname} />
                </div>
        
                <div class="flex flex-col items-start gap-2">
                    <Label for="created_on">{m.created_on()}</Label>
                    <Input id="created_on" type="text" readonly value={getLocalFormatedDate(templateData?.createdOn)} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="created_by">{m.created_by()}</Label>
                    <Input id="created_by" type="text" readonly value={templateData?.createdBy?.firstname + " " + templateData?.createdBy?.lastname} />
                </div>
            </div>
        </Card.Content>
    </Card.Root>
</div>
