<script lang="ts">
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import {Label} from "@/components/ui/label";
    import Loading from "@/components/loading/Loading.svelte";

    let isOobeStatusChecked: boolean = false;

    let firstnameInputValue: string = "";
    let lastnameInputValue: string = "";
    let emailInputValue: string = "";
    let passwordInputValue: string = "";
</script>

<div class="flex items-center justify-center p-10">
    {#if isOobeStatusChecked}
        <form class="flex flex-col items-center justify-between w-full h-full">
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