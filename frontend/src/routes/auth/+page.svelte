<script lang="ts">
    import {Input} from "@/components/ui/input";
    import {Button} from "@/components/ui/button";
    import {Label} from "@/components/ui/label";
    import {m} from "@/paraglide/messages"

    let emailInputValue: string = "";
    let passwordInputValue: string = "";

</script>

<div class="flex items-center justify-center p-10">
    <form class="flex flex-col items-center justify-between w-full h-full">
        <h1 class="text-2xl font-semibold text-center">{m.welcome_back()}</h1>

        <div class="flex flex-col gap-6 w-full">
            <div class="flex flex-col items-start gap-2">
                <Label for="email">{m.email_address()}</Label>
                <Input id="email" type="email" placeholder="john.doe@sendinel.cz" bind:value={emailInputValue} />
            </div>

            <div class="flex flex-col items-start gap-2">
                <Label for="password">{m.password()}</Label>
                <Input id="password" type="password" placeholder="*******" bind:value={passwordInputValue} />
            </div>
        </div>

        <Button type="submit" class="w-full">{m.login()}</Button>
    </form>
</div>