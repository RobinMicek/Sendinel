<script lang="ts">    
    import JsonSchemaEditor from "@/components/json-schema-editor/json-schema-editor.svelte";
    import * as Card from "@/components/ui/card/index.js";
    import { m } from "@/paraglide/messages";
    import type { TemplateResponse } from "@/types/dtos/template";

    export let canEdit: boolean | undefined;
    export let templateData: TemplateResponse;

</script>


<div class="flex flex-col gap-6">
    <Card.Root>
        <Card.Header>
            <Card.Title>{m.schema()}</Card.Title>
            <Card.Description>{m.data_than_can_be_passed_into_this_template_to_personalize_it()}</Card.Description>
        </Card.Header>
        <Card.Content>       
            <div class="flex flex-col gap-6 w-full">
                <JsonSchemaEditor bind:schema={templateData.schema} readonly={!canEdit} />
            </div>
        </Card.Content>
    </Card.Root>
</div>
