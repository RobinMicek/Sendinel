<script lang="ts">
    import * as Card from "@/components/ui/card";
    import picture from '$lib/assets/auth-picture.png';
</script>

<div class="bg-background flex h-screen w-screen flex-col items-center justify-center">
    logo here
    <Card.Root class="overflow-hidden p-0 w-2/5 h-1/2">
        <Card.Content class="grid p-0 md:grid-cols-2 h-full">

            <slot />

            <div class="bg-muted hidden md:block h-full w-full">
                <img
                        src={picture}
                        alt="placeholder"
                        class="h-full w-full object-cover inset-0"
                />
            </div>
        </Card.Content>
    </Card.Root>

    git link here
</div>

