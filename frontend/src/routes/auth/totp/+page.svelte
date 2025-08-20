<script lang="ts">
    import {Button} from "@/components/ui/button";
    import * as InputOTP from "$lib/components/ui/input-otp/index.js";

    let totpCodeInputValue: string = "";
</script>

<div class="flex items-center justify-center p-10">
    <form class="flex flex-col items-center justify-between w-full h-full">
        <h1 class="text-2xl font-semibold text-center">2nd-Factor</h1>

        <p class="text-center opacity-50">Please insert verification code from your authenticator app</p>

        <InputOTP.Root maxlength={6} bind:value={totpCodeInputValue}>
            {#snippet children({ cells })}
                <InputOTP.Group>
                    {#each cells.slice(0, 3) as cell (cell)}
                        <InputOTP.Slot {cell} />
                    {/each}
                </InputOTP.Group>
                <InputOTP.Separator />
                <InputOTP.Group>
                    {#each cells.slice(3, 6) as cell (cell)}
                        <InputOTP.Slot {cell} />
                    {/each}
                </InputOTP.Group>
            {/snippet}
        </InputOTP.Root>


        <Button type="submit" class="w-full">Submit</Button>
    </form>
</div>