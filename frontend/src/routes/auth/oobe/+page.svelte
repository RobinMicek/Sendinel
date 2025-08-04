<script lang="ts">
    import {OobeControllerApi, type UserCreateRequestDto} from "@/backend-sdk";
    import {setIsOobeDone} from "@/utils/storage-util";
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import {Label} from "@/components/ui/label";
    import {triggerAlert} from "@/stores/alert";
    import Loading from "@/components/loading/Loading.svelte"
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";

    let isOobeStatusChecked: boolean = false;

    let firstnameInputValue: string = "";
    let lastnameInputValue: string = "";
    let emailInputValue: string = "";
    let passwordInputValue: string = "";

    const oobeController: OobeControllerApi = new OobeControllerApi();

    async function handleOobe(): Promise<void> {
        const requestBody: UserCreateRequestDto = {
            firstname: firstnameInputValue,
            lastname: lastnameInputValue,
            role: "ADMIN",
            email: emailInputValue,
            password: passwordInputValue
        };

        try {
            const response = await oobeController.createFirstUser(requestBody);
            setIsOobeDone(true);
            triggerAlert("Administrator account successfully created", null, "success");
            goto("/auth");
        } catch (error) {
            triggerAlert("Failed to create an administrator account", error, "error");
        }
    }

    async function checkOobeStatus(): Promise<void> {
        try {
            const response = await oobeController.isOobe();
            if (!response.data.oobe) {
                triggerAlert("OOBE is already done", null, "error");
                goto("/auth");
            }

            isOobeStatusChecked = true;
        } catch (error) {
            triggerAlert("Failed to get OOBE status", error, "error");
        }
    }

    onMount(() => {
        const init = async () => {
            await checkOobeStatus();
        }

        init();
    })
</script>

<div class="flex items-center justify-center p-10">
    {#if isOobeStatusChecked}
        <form class="flex flex-col items-center justify-between w-full h-full" on:submit|preventDefault={handleOobe}>
            <h1 class="text-2xl font-semibold text-center">Welcome to Sendinel</h1>

            <p class="text-center opacity-50">Start by creating your first administrator account</p>

            <div class="flex flex-col gap-6 w-full">
                <div class="flex flex-col items-start gap-2">
                    <Label for="firstname">Firstname</Label>
                    <Input id="firstname" type="text" placeholder="John" bind:value={firstnameInputValue} />
                </div>

                <div class="flex flex-col items-start gap-2">
                    <Label for="lastname">Lastname</Label>
                    <Input id="lastname" type="texts" placeholder="Doe" bind:value={lastnameInputValue} />
                </div>

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

    {:else}
        <Loading />
    {/if}
</div>