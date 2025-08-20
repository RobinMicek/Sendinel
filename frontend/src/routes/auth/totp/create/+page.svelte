<script lang="ts">
    import type {UserTotpCreateResponse, UserTotpStatusResponse} from "@/types/dtos/user";
    import {Button} from "@/components/ui/button";
    import * as InputOTP from "$lib/components/ui/input-otp/index.js";
    import {Input} from "@/components/ui/input";
    import {Skeleton} from "@/components/ui/skeleton";
    import Loading from "@/components/loading/Loading.svelte";
    
    let totpStatus: UserTotpStatusResponse | undefined = undefined;
    let totpCreateInfo: UserTotpCreateResponse | undefined = undefined;

    let continueToActivation: boolean = false;
    let totpCodeInputValue: string = "";

</script>


<div class="flex items-center justify-center p-10">
    {#if totpStatus}
        {#if !continueToActivation}
            <div class="flex flex-col items-center justify-between w-full h-full">
                <h1 class="text-2xl font-semibold text-center">Create 2nd-Factor</h1>

                <p class="text-center opacity-50">Scan this QR code with your authenticator app</p>

                {#if totpCreateInfo}
                    <img
                            src={"data:image/png;base64," + totpCreateInfo.qrBase64}
                            alt="TOTP QR code"
                            class="w-1/2 h-auto aspect-square rounded-md"
                    />

                    <Input id="secret" value={totpCreateInfo.secret} disabled></Input>

                {:else}
                    <Skeleton class="w-1/2 h-auto aspect-square" />
                    <Skeleton class="w-full h-8" />
                {/if}

                <Button type="button" class="w-full" onclick={() => continueToActivation = true}>
                    Continue
                </Button>
            </div>


        {:else}
            <form class="flex flex-col items-center justify-between w-full h-full">
                <h1 class="text-2xl font-semibold text-center">Create 2nd-Factor</h1>

                <p class="text-center opacity-50">Activate your TOTP by inserting verification code from your authenticator app</p>

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
        {/if}
    {:else}
        <Loading />
    {/if}
</div>