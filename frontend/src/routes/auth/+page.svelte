<script lang="ts">
    import type {LoginRequestDto} from "@/backend-sdk";
    import {AuthControllerApi} from "@/backend-sdk";
    import {getToken, setToken} from "@/utils/storage-util";
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import {Label} from "@/components/ui/label";
    import {triggerAlert} from "@/stores/alert";
    import {UnauthenticatedError} from "@/exceptions/unauthenticated-error";
    import {goto} from "$app/navigation";

    let emailInputValue: string = "";
    let passwordInputValue: string = "";

    const authController: AuthControllerApi = new AuthControllerApi();

    async function handleLogin(): Promise<void> {
        const requestBody: LoginRequestDto = {
            email: emailInputValue,
            password: passwordInputValue
        };

        try {
            const response = await authController.login(requestBody);

            if (!response.data.jwtToken) {
                throw new UnauthenticatedError("JWT token is not present in response");
            }

            setToken(response.data.jwtToken);
            triggerAlert("Login successful", null, "success");

            const hasTOTP = await getTotpStatus();

            if (hasTOTP) {
                goto("/auth/totp");
            } else {
                goto("/auth/totp/create");
            }
        } catch (error) {
            triggerAlert("Login failed", error, "error");
        }
    }


    async function getTotpStatus(): Promise<boolean | undefined> {
        try {
            const response = await authController.getTotpStatus({
                headers: {
                    Authorization: getToken()
                }
            });
            return response.data.exists && response.data.activated;
        } catch (error) {
            triggerAlert("Failed to get TOTP status", error, "error");
            throw new UnauthenticatedError("Failed to get TOTP status");
        }
    }

</script>

<div class="flex items-center justify-center p-10">
    <form class="flex flex-col items-center justify-between w-full h-full" on:submit|preventDefault={handleLogin}>
        <h1 class="text-2xl font-semibold text-center">Welcome back</h1>

        <div class="flex flex-col gap-6 w-full">
            <div class="flex flex-col items-start gap-2">
                <Label for="email">Email address</Label>
                <Input id="email" type="email" placeholder="john.doe@sendinel.cz" bind:value={emailInputValue} />
            </div>

            <div class="flex flex-col items-start gap-2">
                <Label for="password">Password</Label>
                <Input id="password" type="password" placeholder="*******" bind:value={passwordInputValue} />
            </div>
        </div>

        <Button type="submit" class="w-full">Log In</Button>
    </form>
</div>