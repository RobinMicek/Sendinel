<script lang="ts">
    import * as ToggleGroup from "$lib/components/ui/toggle-group/index.js";
    import * as Card from "@/components/ui/card/index.js";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import { m } from "@/paraglide/messages";
    import type { TemplateResponse } from "@/types/dtos/template";
    import Monaco from "@/components/monaco/monaco.svelte";
    import { mode } from "mode-watcher";
    import { CaseUpper, CodeXml, Columns2, Eye, Maximize2, Minimize2, Pencil } from "@lucide/svelte";
    import { renderHandlebarsTemplate } from "@/utils/handlebars-utils";
    import { generateExampleFromSchema } from "@/utils/json-schema-utils";
    import Toggle from "@/components/ui/toggle/toggle.svelte";
    import TemplatingInfo from "./tab-edit/templating-info.svelte";
    import ContextVariablesEdit from "./tab-edit/context-variables-edit.svelte";

    export let canEdit: boolean | undefined;
    export let templateData: TemplateResponse;
    
    let templateContextExample = generateExampleFromSchema(templateData.schema)

    let layout: "editor" | "preview" | "both" = "both";
    let currentTemplate: "text" | "html" = templateData.htmlRaw ? "html" : "text";
    let fullscreen: boolean = false
    
    $: renderedTemplate = (() => {
        try {
            return renderHandlebarsTemplate(
                currentTemplate === "html" ? templateData.htmlRaw : "<pre>" + templateData.textRaw + "</pre>",
                templateContextExample
            );
        } catch (e) {
            console.error(e);
            return m.template_could_not_be_rendered_you_probably_have_an_unfinished_handlebars_directive();
        }
    })();

    $: renderedSubject = (() => {
        try {
            return renderHandlebarsTemplate(
                templateData.subject,
                templateContextExample
            );
        } catch (e) {
            console.error(e);
            return m.template_could_not_be_rendered_you_probably_have_an_unfinished_handlebars_directive();
        }
    })();

</script>


<div class="flex flex-col gap-6">
    <Card.Root>
        <Card.Header>
            <Card.Title>{m.subject()}</Card.Title>
        </Card.Header>
        <Card.Content>       
            <div class="grid md:grid-cols-2 gap-6 w-full">
                <div class="flex flex-col items-start gap-2">
                    <Label for="subject">{m.subject()}</Label>
                    <Input id="subject" type="text" placeholder={m.thank_you_for_your_order({orderId: "{{orderId}}"})} readonly={!canEdit} bind:value={templateData.subject} />
                </div>
                    
                <div class="flex flex-col items-start gap-2">
                    <Label for="subject-preview">{m.preview()}</Label>
                    <Input id="subject-preview" type="text" placeholder={m.thank_you_for_your_order({orderId: "0042"})} readonly value={renderedSubject} />
                </div>
            </div>
        </Card.Content>
    </Card.Root>

    <Card.Root>
        <Card.Header>
            <Card.Title>{m.template()}</Card.Title>
            <Card.Description>{m.define_how_your_emails_look()}</Card.Description>
        </Card.Header>
        <Card.Content class={fullscreen ? "absolute top-0 right-0 w-screen h-screen z-10 p-5 bg-background" : ""}>
            <div class="flex justify-between mb-4">
                <ToggleGroup.Root type="single" variant="outline" bind:value={currentTemplate}>
                    <ToggleGroup.Item class="hover:cursor-pointer" value="html"><CodeXml /></ToggleGroup.Item>                    
                    <ToggleGroup.Item class="hover:cursor-pointer" value="text"><CaseUpper /></ToggleGroup.Item>
                </ToggleGroup.Root>

                <div class="flex gap-2">
                    <ToggleGroup.Root type="single" variant="outline" bind:value={layout}>
                        <ToggleGroup.Item class="hover:cursor-pointer" value="editor"><Pencil /></ToggleGroup.Item>
                        <ToggleGroup.Item class="hover:cursor-pointer" value="both"><Columns2 /></ToggleGroup.Item>
                        <ToggleGroup.Item class="hover:cursor-pointer" value="preview"><Eye /></ToggleGroup.Item>
                    </ToggleGroup.Root>                        

                    <Toggle class="hover:cursor-pointer" variant="outline" bind:pressed={fullscreen}>
                        {#if fullscreen}
                            <Minimize2 />
                        {:else}
                            <Maximize2 />
                        {/if}
                    </Toggle>

                    <ContextVariablesEdit bind:context={templateContextExample} />

                    <TemplatingInfo />
                </div>
            </div>

            <div class="flex flex-col md:flex-row h-[90vh]">
                {#if layout !== "preview"}
                    <div class={layout === "both" ? "h-[45vh] md:h-full w-full md:w-1/2" : "w-full"}>
                        {#if currentTemplate === "html"}
                            <Monaco
                                bind:value={templateData.htmlRaw} 
                                readOnly={!canEdit}
                                language="html" 
                                theme={mode.current === "light" ? "vs-light" : "vs-dark"} 
                            />
                        {:else}
                            <Monaco 
                                bind:value={templateData.textRaw} 
                                readOnly={!canEdit}
                                theme={mode.current === "light" ? "vs-light" : "vs-dark"} 
                            />
                        {/if}
                    </div>
                {/if}

                {#if layout !== "editor"}
                    <div class={layout === "both" ? "h-[45vh] md:h-full w-full md:w-1/2" : "w-full"}>                    
                        <div title="template-preview" class="h-full w-full overflow-y-scroll">
                            <iframe class="w-full h-full overflow-y-scrollbar" title="Render" srcdoc={renderedTemplate}></iframe>                        
                        </div>
                    </div>
                {/if}
            </div>                
        </Card.Content>
    </Card.Root>
</div>
