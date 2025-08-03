<script lang="ts">
    import {AuthControllerApi} from "@/backend-sdk";
    import type {TotpRequestDto, UserTotpCreateResponseDto, UserTotpStatusResponseDto} from "@/backend-sdk";
    import {getToken, setToken, setUser} from "@/utils/storage-util";
    import {Button} from "@/components/ui/button";
    import {triggerAlert} from "@/stores/alert";
    import {UnauthenticatedError} from "@/exceptions/unauthenticated-error";
    import * as InputOTP from "$lib/components/ui/input-otp/index.js";
    import {onMount} from "svelte";
    import {Input} from "@/components/ui/input";
    import {Label} from "@/components/ui/label";
    import {goto} from "$app/navigation";

    let totpStatus: UserTotpStatusResponseDto | undefined = undefined;
    let totpCreateInfo: UserTotpCreateResponseDto | undefined = undefined;

    let continueToActivation: boolean = false;
    let totpCodeInputValue: string = "";

    const authController: AuthControllerApi = new AuthControllerApi();

    async function getTotpStatus(): Promise<void> {
        try {
            const response = await authController.getTotpStatus({
                headers: {
                    Authorization: getToken()
                }
            });
            totpStatus = response.data;

            if (totpStatus.exists && totpStatus.activated) {
                triggerAlert("2nd-Factor is already configured for this account", null, "info")
                goto("/auth/totp");
                return;
            }

            await getTotpCreateInfo();
        } catch (error) {
            triggerAlert("Failed to get TOTP status", error, "error");
            throw new UnauthenticatedError("Failed to get TOTP status");
        }
    }

    async function getTotpCreateInfo(): Promise<void> {
        try {
            const response = await authController.createTotp({
                headers: {
                    Authorization: getToken()
                }
            });
            totpCreateInfo = response.data;
        } catch (error) {
            triggerAlert("Failed to create TOTP", error, "error");
        }
    }

    async function activateTotp(): Promise<void> {
        const totpRequest: TotpRequestDto = {
            code: totpCodeInputValue
        }

        try {
            const response = await authController.activateTotp(totpRequest, {
                headers: {
                    Authorization: getToken()
                }
            })
            triggerAlert("TOTP activated", null, "success")
            goto("/auth/totp");
        } catch(error) {
            triggerAlert("Failed to activate TOTP", error, "error")
        }
    }

    onMount(() => {
        getTotpStatus();
    });
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
                {/if}

                <Button type="button" class="w-full" onclick={() => continueToActivation = true}>
                    Continue
                </Button>
            </div>


        {:else}
            <form class="flex flex-col items-center justify-between w-full h-full" on:submit|preventDefault={activateTotp}>
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
        loading
    {/if}
</div>