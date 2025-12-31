
import Link from "next/link";

export function Footer() {
    return (
        <footer role="contentinfo" className="border-t mt-10">
          <div className="max-w-7xl mx-auto px-8 py-12">
            
            {/* Main Footer Content */}
            <div className="flex flex-col md:flex-row justify-between items-start gap-12 mb-8">
              
              {/* Brand */}
              <div className="flex-shrink-0">
                <h3 className="text-xl font-semibold mb-2">Nivara</h3>
                <p className="text-sm text-gray-500">
                  Seamless hotel reservations.
                </p>
              </div>

              {/* Links */}
              <div className="flex flex-wrap gap-x-12 gap-y-6">
                <div className="space-y-3">
                  <Link href="/hotels" className="block text-sm text-gray-600 hover:text-gray-900">
                    Hotels
                  </Link>
                  <Link href="/bookings" className="block text-sm text-gray-600 hover:text-gray-900">
                    Bookings
                  </Link>
                  <Link href="/about" className="block text-sm text-gray-600 hover:text-gray-900">
                    About
                  </Link>
                </div>

                <div className="space-y-3">
                  <Link href="/help" className="block text-sm text-gray-600 hover:text-gray-900">
                    Help
                  </Link>
                  <Link href="/privacy" className="block text-sm text-gray-600 hover:text-gray-900">
                    Privacy
                  </Link>
                  <Link href="/terms" className="block text-sm text-gray-600 hover:text-gray-900">
                    Terms
                  </Link>
                </div>
              </div>
            </div>

            {/* Bottom */}
            <div className="pt-8 border-t">
              <p className="text-sm text-gray-500">
                © {new Date().getFullYear()} Nivara. All rights reserved.
              </p>
            </div>

          </div>
        </footer>
    );
} 