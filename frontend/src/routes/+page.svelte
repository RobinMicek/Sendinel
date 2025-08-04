<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import Loading from "@/components/loading/Loading.svelte";
    import {OobeControllerApi, type UserResponseDto} from "@/backend-sdk";
    import {getIsOobeDone, getUser, setIsOobeDone} from "@/utils/storage-util";
    import {triggerAlert} from "@/stores/alert";

    async function getOobeStatus(): Promise<boolean | undefined> {
        const oobeController: OobeControllerApi = new OobeControllerApi();

        try {
            const response = await oobeController.isOobe();
            return response.data.oobe;
        } catch (error) {
            triggerAlert("Failed to get OOBE status", error, "error");
        }
    }

    function route(): void {
        if (!getIsOobeDone()) {
            goto("/auth/oobe");
            return;
        }

        try {
            const user: UserResponseDto = getUser();
            triggerAlert(
                "Logged in",
                `Logged in as: ${user.firstname} ${user.lastname}`,
                "success"
            );
        } catch {
            goto("/auth");
        }
    }

    onMount(() => {
        const init = async () => {
            if (getIsOobeDone() == null) {
                const status = await getOobeStatus();
                setIsOobeDone(!status);
            }
            route();
        };

        init();
    });


</script>

<div class="w-screen h-screen flex justify-center items-center">
    <Loading />
</div>