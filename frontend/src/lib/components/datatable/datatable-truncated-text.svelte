<script lang="ts">
  export let text: string;
</script>

<span
    class="block max-w-[300px] truncate"
    title={text}
>
    {text}
</span>