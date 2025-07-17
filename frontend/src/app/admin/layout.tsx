// ... 기존 코드 삭제 ...
import AdminLayout from "./AdminLayout";

export default function Layout({ children }: { children: React.ReactNode }) {
  return <AdminLayout>{children}</AdminLayout>;
}
