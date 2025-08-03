<script lang="ts">
    import {type TotpRequestDto, UserControllerApi, type UserResponseDto} from "@/backend-sdk";
    import {AuthControllerApi} from "@/backend-sdk";
    import {getToken, setToken, setUser} from "@/utils/storage-util";
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import * as InputOTP from "$lib/components/ui/input-otp/index.js";
    import {triggerAlert} from "@/stores/alert";
    import {UnauthenticatedError} from "@/exceptions/unauthenticated-error";
    import {goto} from "$app/navigation";

    let totpCodeInputValue: string = "";

    async function handleTotp(): Promise<void> {
        const authController: AuthControllerApi = new AuthControllerApi();
        const totpRequest: TotpRequestDto = {
            code: totpCodeInputValue
        };

        try {
            const response = await authController.verifyTotp(getToken(), totpRequest);

            if (!response.data.jwtToken) {
                throw new UnauthenticatedError("JWT token is not present in response");
            }

            setToken(response.data.jwtToken);

            const userInfo = await getCurrentUser();
            if (!userInfo) {
                throw new UnauthenticatedError("Failed to get user info");
            }

            setUser(userInfo);
            triggerAlert("2nd-Factor verified", null, "success");
            goto("/");
        } catch (error) {
            triggerAlert("2nd-Factor verification failed", error, "error");
        }
    }

    async function getCurrentUser(): Promise<UserResponseDto | undefined> {
        const userController: UserControllerApi = new UserControllerApi();

        try {
            const response = await userController.getCurrentUser({
                headers: {
                    Authorization: getToken()
                }
            });
            return response.data;
        } catch (error) {
            throw new UnauthenticatedError("Failed to get current user info: " + error);
        }
    }


</script>

<div class="flex items-center justify-center p-10">
    <form class="flex flex-col items-center justify-between w-full h-full" on:submit|preventDefault={handleTotp}>
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