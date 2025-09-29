<script lang="ts">
    import * as Select from "@/components/ui/select/index.js"
	import { type JsonSchemaNode } from "@/types/json-schema-node";
	import { getContext } from "svelte";
    import Input from "@/components/ui/input/input.svelte";
    import { m } from "@/paraglide/messages";
    import Button from "../ui/button/button.svelte";
    import { Asterisk, Plus, Trash, Trash2 } from "@lucide/svelte";
    import Toggle from "../ui/toggle/toggle.svelte";
    import Confirm from "../confirm/confirm.svelte";

	export let node: JsonSchemaNode;
	export let readonly: boolean = false;

    export let isFirst: boolean = true

    // Add a child node
	function addChild() {
		if (!node.children) node.children = [];
		node.children = [
			...node.children,
			{
				name: "newField",
				type: "string",
				required: false,
				children: [] // Ensure new children have a children array
			}
		];
	}

	// Remove a child node by index
	function removeChild(index: number) {
		if (!node.children) return;
		node.children = node.children.filter((_, i) => i !== index);
	}

    $: {
        if (["object", "list"].includes(node.type)) {
            if (!node.children) {
                node = { ...node, children: [] };
            }
        } else if (node.children?.length) {
            node = { ...node, children: [] };
        }
    }

</script>

<div class="w-full">
    <div class="flex gap-6 w-full items-center">
        <!-- Delete All Button -->
        {#if !readonly && isFirst && node.children?.length != 0}
            

            <Confirm
                triggerVariant="destructive"
                triggerText=""
                triggerIcon={Trash2}
                contentText={m.do_you_really_want_to_delete_the_whole_schema()}                
                disabled={readonly}
                action={() => node.children = []}
            />
        {/if}
        
        <!-- Add Child Button -->
        {#if !readonly && ["list", "object"].includes(node.type)}
            <Button class="hover:cursor-pointer" onclick={addChild} type="button">
                <Plus />
            </Button>
        {/if}
    
        <!-- Node Name - Needs to update on escape, if you bind the value then it constantly unfocuses the input  -->
        <Input type="text" class="min-w-64" value={node.name} onblur={(event) => {node.name = (event.currentTarget as HTMLInputElement).value}} readonly={readonly || isFirst} />

        <!-- Node Type -->
        <Select.Root
            type="single"
            disabled={readonly || isFirst}
            value={node.type}
            onValueChange={(value) => {
                node.type = value as "string" | "number" | "boolean" | "list" | "object"
            }}
        >
            <Select.Trigger>
                {{
                    string: m.string(),
                    number: m.number(),
                    boolean: m.boolean(),
                    list: m.list(),
                    object: m.object(),
                }[node.type]}
            </Select.Trigger>

            <Select.Content id="type">
                <Select.Item value="string">{m.string()}</Select.Item>
                <Select.Item value="number">{m.number()}</Select.Item>
                <Select.Item value="boolean">{m.boolean()}</Select.Item>
                <Select.Item value="list">{m.list()}</Select.Item>
                <Select.Item value="object">{m.object()}</Select.Item>
            </Select.Content>
        </Select.Root>


        <!-- Required -->
        <Toggle class="hover:cursor-pointer" variant="outline" bind:pressed={node.required} disabled={readonly || isFirst}>
            <Asterisk />
        </Toggle>
    </div>

    

    <!-- Children -->
    <div class="ml-10">
        {#if node.children && node.children.length > 0}
            {#each node.children as child, index (child.name + index)}
            <div class="flex gap-6 w-full pt-4">
                {#if !readonly}
                    <Button class="hover:cursor-pointer" variant="destructive" onclick={() => removeChild(index)} type="button">
                        <Trash />
                    </Button>
                {/if}

                <svelte:self bind:node={child} isFirst={false} {readonly} />
            </div>
            {/each}
        {/if}
    </div>
</div>
