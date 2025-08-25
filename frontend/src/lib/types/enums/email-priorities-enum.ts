import { m } from "@/paraglide/messages";

export enum EmailPrioritiesEnum {
  LOWEST = "LOWEST",
  LOW = "LOW",
  NORMAL = "NORMAL",
  HIGH = "HIGH",
  HIGHEST = "HIGHEST",
}

type EmailPriorityMeta = {
  translation: string;      // translated text
  color: string;            // tailwind color
  priorityNumber: number
};

export const emailPrioritiesMeta: Record<EmailPrioritiesEnum, EmailPriorityMeta> = {
  [EmailPrioritiesEnum.LOWEST]: {
    translation: m.lowest(),
    color: "gray-500",
    priorityNumber: 0
  },
  [EmailPrioritiesEnum.LOW]: {
    translation: m.low(),
    color: "blue-500",
    priorityNumber: 1
  },
  [EmailPrioritiesEnum.NORMAL]: {
    translation: m.normal(),
    color: "green-500",
    priorityNumber: 2
  },
  [EmailPrioritiesEnum.HIGH]: {
    translation: m.high(),
    color: "orange-500",
    priorityNumber: 3
  },
  [EmailPrioritiesEnum.HIGHEST]: {
    translation: m.highest(),
    color: "red-600",
    priorityNumber: 4
  },
};