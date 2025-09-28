
import Link from "next/link";

export function Footer() {
    return (
        <footer role="contentinfo" className="pt-6 pb-8 text-center text-sm border-t">
          <div className="mx-auto px-4">
            <p>© {new Date().getFullYear()} Hotel Reservation System. All rights reserved.</p>
            <p className="mt-2">
              <Link href="/privacy" className="underline hover:text-foreground">Privacy</Link>
                <span className="mx-2">·</span>
              <Link href="/terms" className="underline hover:text-foreground">Terms</Link>
            </p>
          </div>
        </footer>
    );
} 