<script lang="ts">
    import * as Dialog from "@/components/ui/dialog/index.js";
    import { m } from "@/paraglide/messages";
    import Button from "@/components/ui/button/button.svelte";
    import { Variable } from "@lucide/svelte";
    import { JSONEditor, Mode, type JSONContent, type Content } from "svelte-jsoneditor";
    import { mode } from "mode-watcher";

    export let context: object

    // Editor version of context
    let editorContent: JSONContent = { json: context }

    function handleChange(updatedContent: Content, previousContent: Content) {
        // content is an object { json: unknown } | { text: string }
        context = (updatedContent as JSONContent).json as object
        editorContent = updatedContent as JSONContent
    }
</script>
<Dialog.Root>
    <Dialog.Trigger>
        <Button type="button" variant="outline" class={`hover:cursor-pointer`}>
            <Variable />
        </Button>
    </Dialog.Trigger>
    <Dialog.Content class="max-h-screen overflow-y-scroll">
        <Dialog.Header>
            <Dialog.Title>{m.demo_context()}</Dialog.Title>
            <Dialog.Description>{m.these_variables_are_used_to_render_preview_of_this_template_they_are_not_stored_anywhere_and_will_not_be_used_to_render_actual_emails()}</Dialog.Description>
        </Dialog.Header>
        
        <div class={mode.current != "light" ? "jse-theme-dark" : ""}>
            <JSONEditor
                content={editorContent}
                mode={Mode.tree}
                mainMenuBar={false}
                navigationBar={false}
                statusBar={true}
                onChange={handleChange}
                onRenderMenu={() => []}
                onRenderContextMenu={() => false}
            />
        </div>
        
        <style>
            @import 'svelte-jsoneditor/themes/jse-theme-dark.css';
        </style>
    </Dialog.Content>
</Dialog.Root>
