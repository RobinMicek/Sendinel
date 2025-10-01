import { m } from "@/paraglide/messages";
import Plus from "@lucide/svelte/icons/file";
import Clock from "@lucide/svelte/icons/clock";
import Wrench from "@lucide/svelte/icons/wrench";
import Lock from "@lucide/svelte/icons/lock";
import X from "@lucide/svelte/icons/x";
import Send from "@lucide/svelte/icons/send";
import Pause from "@lucide/svelte/icons/pause";
import CornerUpLeft from "@lucide/svelte/icons/corner-up-left";
import Check from "@lucide/svelte/icons/check";
import Eye from "@lucide/svelte/icons/eye";
import MessageCircleQuestion from "@lucide/svelte/icons/message-circle-question";
import { Repeat, type Component } from '@lucide/svelte';

export enum EmailStatusesEnum {
    CREATED = "CREATED",
    ENQUEUED = "ENQUEUED",

    INVALID_CONFIGURATION = "INVALID_CONFIGURATION",
    UNAUTHORIZED = "UNAUTHORIZED",

    FAILED = "FAILED",
    SENT = "SENT",

    DEFERRED = "DEFERRED",
    BOUNCED = "BOUNCED",
    DELIVERED = "DELIVERED",

    OPENED = "OPENED",

    RESEND_REQUESTED = "RESEND_REQUESTED",

    UNKNOWN = "UNKNOWN",
}

type EmailStatusMeta = {
    translation: string;        // translated text
    color: string;              // tailwind stroke color (e.g. stroke-red-500, used for lucide icons)
    icon: typeof Component;     // lucide icon component
};

export const emailStatusesMeta: Record<EmailStatusesEnum, EmailStatusMeta> = {
    [EmailStatusesEnum.CREATED]: {
        translation: m.created(),
        color: "stroke-stone-500",
        icon: Plus,
    },
    [EmailStatusesEnum.ENQUEUED]: {
        translation: m.enqueued(),
        color: "stroke-slate-500",
        icon: Clock,
    },
    [EmailStatusesEnum.INVALID_CONFIGURATION]: {
        translation: m.invalid_configuration(),
        color: "stroke-red-600",
        icon: Wrench,
    },
    [EmailStatusesEnum.UNAUTHORIZED]: {
        translation: m.unauthorized(),
        color: "stroke-orange-600",
        icon: Lock,
    },
    [EmailStatusesEnum.FAILED]: {
        translation: m.failed(),
        color: "stroke-red-700",
        icon: X,
    },
    [EmailStatusesEnum.SENT]: {
        translation: m.sent(),
        color: "stroke-green-500",
        icon: Send,
    },
    [EmailStatusesEnum.DEFERRED]: {
        translation: m.deferred(),
        color: "stroke-yellow-500",
        icon: Pause,
    },
    [EmailStatusesEnum.BOUNCED]: {
        translation: m.bounced(),
        color: "stroke-pink-500",
        icon: CornerUpLeft,
    },
    [EmailStatusesEnum.DELIVERED]: {
        translation: m.delivered(),
        color: "stroke-green-600",
        icon: Check,
    },
    [EmailStatusesEnum.OPENED]: {
        translation: m.opened(),
        color: "stroke-purple-500",
        icon: Eye,
    },
    [EmailStatusesEnum.RESEND_REQUESTED]: {
        translation: m.resend_requested(),
        color: "stroke-stone-300",
        icon: Repeat,
    },
    [EmailStatusesEnum.UNKNOWN]: {
        translation: m.unknown(),
        color: "stroke-slate-500",
        icon: MessageCircleQuestion,
    },
};