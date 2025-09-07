<script lang="ts">
    import * as Dialog from "@/components/ui/dialog/index.js";
    import { m } from "@/paraglide/messages";
    import Button from "@/components/ui/button/button.svelte";
    import { Info } from "@lucide/svelte";
    import { APP_NAME } from "@/config";
</script>
<Dialog.Root>
    <Dialog.Trigger>
        <Button type="button" variant="outline" class={`hover:cursor-pointer`}>
            <Info />
        </Button>
    </Dialog.Trigger>
    <Dialog.Content>
        <Dialog.Header>
            <Dialog.Title>{m.templating_information()}</Dialog.Title>
            <Dialog.Description>{m.how_to_create_templates()}</Dialog.Description>
        </Dialog.Header>
        <p class="text-justify text-sm">{m.how_to_create_templates_description({app_name: APP_NAME, schema_tab_name: m.schema()})}</p>
    </Dialog.Content>
</Dialog.Root>
