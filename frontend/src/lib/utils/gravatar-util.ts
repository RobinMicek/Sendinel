  import md5 from 'blueimp-md5';

export function getGravatarUrl(email: string, size: number = 200): string {
    return `https://www.gravatar.com/avatar/${md5(email.trim().toLowerCase())}?s=${size}&d=identicon`;
}