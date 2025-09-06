<script lang="ts">
    import { onMount } from "svelte";
    import * as monaco from "monaco-editor";

    export let value: string = "";
    export let language: "html" | "handlebars" | "" = "";
    export let theme: string = "vs-dark";
    export let readOnly: boolean = false

    let container: HTMLDivElement;
    let editor: monaco.editor.IStandaloneCodeEditor | null = null;

    function setupEditor() {
        editor = monaco.editor.create(container, {
            value,
            language,
            theme,
            automaticLayout: true,
            readOnly
        });

        editor.onDidChangeModelContent(() => {
            value = editor!.getValue();
        });
    }

    onMount(() => {
        self.MonacoEnvironment = {
            getWorker: (_moduleId: unknown, label: string) => {
                if (label === 'html' || label === 'handlebars') {
                    return new Worker(new URL('monaco-editor/esm/vs/language/html/html.worker.js', import.meta.url), { type: 'module' });
                }              
                return new Worker(new URL('monaco-editor/esm/vs/editor/editor.worker.js', import.meta.url), { type: 'module' });
            }
        };

        setupEditor();

        return () => editor?.dispose();
    });

    // Sync parent → editor
    $: if (editor && editor.getValue() !== value) {
        editor.setValue(value);
    }
</script>

<div bind:this={container} class="editor"></div>

<style>
.editor {
    width: 100%;
    height: 100%;
}
</style>
