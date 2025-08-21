<script>
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";

</script>
<div class="flex flex-1 flex-col gap-4 p-4 pt-0">
    <div class="grid grid-cols-4 gap-4">
        <Skeleton class="aspect-video" />
        <Skeleton class="aspect-video" />
        <Skeleton class="aspect-video" />
        <Skeleton class="aspect-video" />
    </div>

    <Skeleton class="aspect-video" />
</div>